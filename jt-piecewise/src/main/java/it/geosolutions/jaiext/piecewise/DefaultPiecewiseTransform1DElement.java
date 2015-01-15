/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package it.geosolutions.jaiext.piecewise;

import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;

/**
 * Convenience implementation of the   {@link DefaultPiecewiseTransform1DElement}   .
 * @author   Simone Giannecchini, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class DefaultPiecewiseTransform1DElement extends DefaultDomainElement1D implements
		PiecewiseTransform1DElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7422178060824402864L;
	/**
     * The math transform
     * @uml.property  name="transform"
     */
	private MathTransformation transform;

	/**
	 * Inverse {@link MathTransformation}
	 */
	private MathTransformation inverse;
        
	private int hashCode=-1;

	
	/**
	 * Builds up a {@link DefaultPiecewiseTransform1DElement} which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final Range inRange,
			final double value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	/**
	 * Builds up a DefaultPiecewiseTransform1DElement which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final Range inRange,
			final byte value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	/**
	 * Builds up a DefaultPiecewiseTransform1DElement which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final Range inRange,
			final int value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 * @param inRange
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 * @param outRange
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name, 
			final Range inRange,
			final Range outRange) {	
		return new DefaultLinearPiecewiseTransform1DElement(name,inRange,outRange);
	}
	
	
	/**
	 * Creates a pass-through DefaultPiecewiseTransform1DElement.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name)
			throws IllegalArgumentException {
		return new DefaultPassthroughPiecewiseTransform1DElement(name,  RangeFactory.create(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true, true));
	}

	/**
	 * Creates a pass-through DefaultPiecewiseTransform1DElement.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	public  static DefaultPiecewiseTransform1DElement create(
			final CharSequence name, 
			final Range valueRange)
			throws IllegalArgumentException {
		return new DefaultPassthroughPiecewiseTransform1DElement(name,  valueRange);
	}	
	/**
	 * Protected constructor for {@link DomainElement1D}s that want to build their
	 * transform later on.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	protected DefaultPiecewiseTransform1DElement(CharSequence name, Range valueRange)
			throws IllegalArgumentException {
		super(name, valueRange);
	}

	/**
	 * Public constructor for building a {@link DomainElement1D} which applies the
	 * specified transformation on the values that fall into its definition
	 * range.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @param transform
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	protected DefaultPiecewiseTransform1DElement(CharSequence name, Range valueRange,
			final MathTransformation transform) throws IllegalArgumentException {
		super(name, valueRange);
		// /////////////////////////////////////////////////////////////////////
		//
		// Initial checks
		//
		// /////////////////////////////////////////////////////////////////////
		PiecewiseUtilities.ensureNonNull("transform", transform);
		this.transform = transform;
	}

	/**
     * Getter for the underlying  {@link MathTransformation}  .
     * @return  the underlying  {@link MathTransformation}  .
     * @uml.property  name="transform"
     */
	protected synchronized MathTransformation getTransform() {
		return transform;
	}

	/**
	 * Transforms the specified value.
	 * 
	 * @param value
	 *            The value to transform.
	 * @return the transformed value.
	 * @throws TransformationException
	 *             if the value can't be transformed.
	 */
	public synchronized double transform(double value)
			throws TransformationException {

		if (transform == null)
			throw new IllegalStateException();

		if (contains(value))
			return transform.transform(value);

		throw new IllegalArgumentException("Provided value is not contained in this domain");
	}

	/**
	 * Gets the derivative of this function at a value.
	 * 
	 * @param value
	 *            The value where to evaluate the derivative.
	 * @return The derivative at the specified point.
	 * @throws TransformationException
	 *             if the derivative can't be evaluated at the specified point.
	 */
	public synchronized double derivative(double value)
			throws TransformationException {
		if (transform == null)
			//  @todo TODO
			throw new IllegalStateException();
		if (contains(value))
			return transform.derivative(value);
		throw new TransformationException("Unable to evaluate value: " + value);
	}

	/**
	 * Transforms the specified {@code ptSrc} and stores the result in
	 * {@code ptDst}.
	 */
	public DirectPosition transform(final DirectPosition ptSrc,
			DirectPosition ptDst) throws TransformationException {
		if (ptDst == null) {
			ptDst = new DirectPosition();
		}
		ptDst.setOrdinate(transform(ptSrc.getOrdinate()));
		return ptDst;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#getSourceDimensions()
	 */
	public int getSourceDimensions() {
		return transform.getSourceDimensions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#getTargetDimensions()
	 */
	public int getTargetDimensions() {
		return transform.getTargetDimensions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#inverse()
	 */
	public synchronized MathTransformation inverse()
			throws NoninvertibleTransformException {
		if (inverse != null)
			return inverse;
		if (transform == null)
			throw new IllegalStateException();
		inverse = (MathTransformation) transform.inverse();
		return inverse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#isIdentity()
	 */
	public boolean isIdentity() {
		return transform.isIdentity();

	}

	/**
     * @param  mathTransform
     * @uml.property  name="inverse"
     */
	protected synchronized void setInverse(MathTransformation mathTransform) {
		if (this.inverse == null)
			this.inverse = mathTransform;
		else
			throw new IllegalStateException();
	}

	/**
     * @param  transform
     * @uml.property  name="transform"
     */
	protected synchronized void setTransform(MathTransformation transform) {
		PiecewiseUtilities.ensureNonNull("transform", transform);
		if (this.transform == null)
			this.transform = transform;
		else
			throw new IllegalStateException();
	}


	public boolean equals(Object obj) {
	    if(obj==this)
			return true;
            if(!(obj instanceof DefaultPiecewiseTransform1DElement))
                return false;
            final DefaultPiecewiseTransform1DElement that=(DefaultPiecewiseTransform1DElement) obj;
            if(getEquivalenceClass()!=(that.getEquivalenceClass()))
                return false;
            if (!PiecewiseUtilities.equals(transform, that.transform))
                return false;
            if (!PiecewiseUtilities.equals(inverse, that.inverse))
                return false;
            return super.equals(obj);
		
	}

    protected Class<?> getEquivalenceClass(){
        return DefaultPiecewiseTransform1DElement.class;
    }

    @Override
    public int hashCode() {
        if(hashCode>=0)
            return hashCode;
        hashCode=37;
        hashCode=PiecewiseUtilities.hash(transform,hashCode);
        hashCode=PiecewiseUtilities.hash( inverse,hashCode);
        hashCode=PiecewiseUtilities.hash( super.hashCode(),hashCode);
        return hashCode;
    }

    public static DefaultPiecewiseTransform1DElement create(String string,
            Range range,
            MathTransformation mathTransform1D) {
        return new DefaultPiecewiseTransform1DElement(string, range, mathTransform1D);
    }

}
