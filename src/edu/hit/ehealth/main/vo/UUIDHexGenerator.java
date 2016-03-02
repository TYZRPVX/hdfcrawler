package edu.hit.ehealth.main.vo;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.type.Type;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Properties;

public class UUIDHexGenerator extends org.hibernate.id.UUIDHexGenerator {
    private String sep = "";

    @Override
    public void configure(Type type, Properties params, Dialect d) {
        sep = ConfigurationHelper.getString("separator", params, "");
//		prefix = ConfigurationHelper.getString( "prefix", params, "" );
    }

    @Override
    public Serializable generate(SessionImplementor session, Object obj) {
        String primaryId = ((AbstractVO) obj).getPrimaryId();
        if (primaryId != null && primaryId.length() > 0) {
            return primaryId;
        }
        String tablename = obj.getClass().getAnnotation(Table.class).name();
        String id = format(tablename).toUpperCase() + sep
                + format(getHiTime()) + sep
                + format(getLoTime()) + sep
                + format(getCount());
        return id;
    }


    private String format(String s) {
        StringBuilder buf = new StringBuilder("0000");
        if (s.length() > buf.length()) {
            return s.substring(0, buf.length());
        } else {
            buf.replace(0, s.length(), s);
            return buf.toString();
        }

    }


}
