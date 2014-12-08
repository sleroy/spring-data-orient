package org.springframework.data.orient.repository.query;

import static org.jooq.impl.DSL.field;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SelectLimitStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.orient.repository.DefaultSource;
import org.springframework.data.orient.repository.OrientSource;
import org.springframework.data.orient.repository.annotation.Cluster;
import org.springframework.data.orient.repository.annotation.Source;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

public class OrientQueryCreator extends AbstractQueryCreator<String, Condition> {

	private final DSLContext	          context;

	private final PartTree	              tree;

	private final OrientParameterAccessor	accessor;

	private final OrientQueryMethod	      method;

	private final ParamType	              paramType;

	private final Class<?>	              domainClass;

	public OrientQueryCreator(final PartTree tree, final OrientQueryMethod method,
	        final OrientParameterAccessor parameters) {
		this(tree, method, parameters, ParamType.NAMED);
	}

	public OrientQueryCreator(final PartTree tree, final OrientQueryMethod method,
	        final OrientParameterAccessor parameters, final ParamType paramType) {
		super(tree, parameters);

		this.method = method;
		this.context = DSL.using(SQLDialect.MYSQL);
		this.tree = tree;
		this.accessor = parameters;
		this.paramType = paramType;
		this.domainClass = method.getEntityInformation().getJavaType();
	}

	public boolean isCountQuery() {
		return this.tree.isCountProjection();
	}

	private <A extends Annotation> A findAnnotation(final Class<A> annotationType) {
		A annotation = AnnotationUtils.findAnnotation(this.method.getMethod(), annotationType);

		if (annotation == null) {
			annotation = AnnotationUtils.findAnnotation(this.method.getRepositoryInterface(), annotationType);
		}

		return annotation;
	}

	private Query limitIfPageable(final SelectLimitStep<? extends Record> limitStep, final Pageable pageable,
	        final Sort sort) {
		if (pageable == null || this.isCountQuery()) {
			return limitStep;
		} else if (sort == null) {
			return limitStep.limit(pageable.getPageSize());
		} else {
			return limitStep.limit(pageable.getPageSize()).offset(pageable.getOffset());
		}
	}

	@SuppressWarnings("incomplete-switch")
	private Condition lowerIfIgnoreCase(final Part part, final Field<Object> field, final Iterator<Object> iterator) {
		switch (part.shouldIgnoreCase()) {
		case ALWAYS:
		case WHEN_POSSIBLE:
			return field.likeIgnoreCase(iterator.next().toString());
		default:
			break;
		}

		return field.like(iterator.next().toString());
	}

	private SelectLimitStep<? extends Record> orderByIfRequired(
	        final SelectConditionStep<? extends Record> conditionStep, final Pageable pageable, final Sort sort) {
		if (this.isCountQuery()) { return conditionStep; }
		if (sort == null) {
			return pageable == null ? conditionStep : conditionStep.and(field("@rid").gt(pageable.getOffset()));
		} else {
			return conditionStep.orderBy(this.toOrders(sort));
		}
	}

	private List<Object> toList(final Iterator<Object> iterator) {
		if (iterator == null || !iterator.hasNext()) { return Collections.emptyList(); }

		final List<Object> list = new ArrayList<Object>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}

		return list;
	}

	private List<SortField<?>> toOrders(final Sort sort) {
		final List<SortField<?>> orders = new ArrayList<SortField<?>>();

		for (final Order order : sort) {
			orders.add(field(order.getProperty()).sort(
			        order.getDirection() == Direction.ASC ? SortOrder.ASC : SortOrder.DESC));
		}

		return orders;
	}

	@Override
	protected Condition and(final Part part, final Condition base, final Iterator<Object> iterator) {
		return base.and(this.toCondition(part, iterator));
	}

	@Override
	protected String complete(final Condition criteria, final Sort sort) {
		final Pageable pageable = this.accessor.getPageable();

		SelectSelectStep<? extends Record> selectStep;

		if (this.isCountQuery()) {
			selectStep = this.context.selectCount();
		} else if (this.tree.isDistinct()) {
			selectStep = this.context.selectDistinct();
		} else {
			selectStep = this.context.select();
		}

		final SelectConditionStep<? extends Record> conditionStep = selectStep.from(
		        QueryUtils.toSource(this.getSource())).where(criteria);

		final SelectLimitStep<? extends Record> limitStep = this.orderByIfRequired(conditionStep, pageable, sort);

		final Query query = this.limitIfPageable(limitStep, pageable, sort);

		// FIXME: Fix it!!
		// String queryString = query.getSQL(paramType);
		// Use inline parameters for paged queries
		final String queryString = pageable == null ? query.getSQL(this.paramType) : query.getSQL(ParamType.INLINED);
		System.out.println(queryString);

		return queryString;
	}

	@Override
	protected Condition create(final Part part, final Iterator<Object> iterator) {
		return this.toCondition(part, iterator);
	}

	protected OrientSource getSource() {
		final OrientSource orientSource = this.accessor.getSource();

		if (orientSource != null) { return orientSource; }

		final Source source = this.findAnnotation(Source.class);
		if (source != null) { return new DefaultSource(source.type(), source.value()); }

		final Cluster cluster = this.findAnnotation(Cluster.class);
		if (cluster != null) { return new DefaultSource(cluster.value()); }

		return new DefaultSource(this.domainClass);
	}

	@Override
	protected Condition or(final Condition base, final Condition criteria) {
		return base.or(criteria);
	}

	protected Condition toCondition(final Part part, final Iterator<Object> iterator) {
		final String property = part.getProperty().toDotPath();
		final Field<Object> field = field(property);

		switch (part.getType()) {
		case AFTER:
		case GREATER_THAN:
			return field.gt(iterator.next());
		case GREATER_THAN_EQUAL:
			return field.ge(iterator.next());
		case BEFORE:
		case LESS_THAN:
			return field.lt(iterator.next());
		case LESS_THAN_EQUAL:
			return field.le(iterator.next());
		case BETWEEN:
			return field.between(iterator.next(), iterator.next());
		case IS_NULL:
			return field.isNull();
		case IS_NOT_NULL:
			return field.isNotNull();
		case IN:
			return field.in(this.toList(iterator));
		case NOT_IN:
			return field.notIn(this.toList(iterator));
		case LIKE:
			return this.lowerIfIgnoreCase(part, field, iterator);
		case NOT_LIKE:
			return this.lowerIfIgnoreCase(part, field, iterator).not();
		case STARTING_WITH:
			return field.startsWith(iterator.next());
		case ENDING_WITH:
			return field.endsWith(iterator.next());
		case CONTAINING:
			return field.contains(iterator.next());
		case SIMPLE_PROPERTY:
			return field.eq(iterator.next());
		case NEGATING_SIMPLE_PROPERTY:
			return field.ne(iterator.next());
		case TRUE:
			return field.eq(true);
		case FALSE:
			return field.eq(false);
		default:
			throw new IllegalArgumentException("Unsupported keyword!");
		}
	}
}
