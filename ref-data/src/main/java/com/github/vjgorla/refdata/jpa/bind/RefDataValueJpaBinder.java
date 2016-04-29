package com.github.vjgorla.refdata.jpa.bind;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;
import com.github.vjgorla.refdata.util.ReflectionUtils;

/**
 * 
 * @author Vijaya Gorla
 */
public class RefDataValueJpaBinder<V extends AbstractRefDataValue> implements UserType, DynamicParameterizedType {
    
    public static final String BINDER = "com.github.vjgorla.refdata.jpa.bind.RefDataValueJpaBinder";
    private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };
    
    private RefDataType<V> type;
    private Class<V> valueClass;
    
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType)parameters.get( PARAMETER_TYPE );
        valueClass = reader.getReturnedClass();
        type = ReflectionUtils.getType(valueClass);
    }

    @Override
    public Class<?> returnedClass() {
        return valueClass;
    }
    
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }
    
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
    
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }
    
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return EqualsHelper.equals(x, y);
    }
    
    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
    
    @Override
    public boolean isMutable() {
        return false;
    }
    
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String s = rs.getString(names[0]);
        if (s != null) {
            return type.decode(s);
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (null == value) {
            st.setNull(index, sqlTypes()[0]);
        } else {
            st.setString(index, ((AbstractRefDataValue)value).getCode());
        }
    }
}