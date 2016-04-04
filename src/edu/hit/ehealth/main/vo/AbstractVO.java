package edu.hit.ehealth.main.vo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 用于CRUD的抽象VO，所有执行数据库操作的VO应继承于本类
 */
@MappedSuperclass
public abstract class AbstractVO implements Serializable {

    public static final long serialVersionUID = 1L;

    private String primaryId;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "edu.hit.ehealth.main.vo.UUIDHexGenerator")
    @Column()
    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    private Date crawlDate;

    private String crawlPageUrl;

    @Column(name = "zqsj")
    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date date) {
        this.crawlDate = date;
    }

    @Column(name = "pageurl")
    public String getCrawlPageUrl() {
        return crawlPageUrl;
    }

    public void setCrawlPageUrl(String crawlPageUrl) {
        this.crawlPageUrl = crawlPageUrl;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            PropertyDescriptor[] pds = Introspector.getBeanInfo(getClass(), Object.class).getPropertyDescriptors();
            for (PropertyDescriptor item : pds) {
                sb.append('[').append(item.getName()).append(':')
                        .append(" ")
                        .append(item.getReadMethod().invoke(this, new Object[]{})).append(']')
                        .append("\n");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public Object getAttributeValue(String name) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, getClass());
            return propertyDescriptor.getReadMethod().invoke(this, new Object[]{});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAttributeValue(String name, Object value) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, getClass());
            propertyDescriptor.getWriteMethod().invoke(this, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    @Transient
    public String[] getAttributeNames() {
        String[] attributeNames = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(getClass(), AbstractVO.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            if (pds != null && pds.length > 0) {
                ArrayList<String> list = new ArrayList<String>();
                for (PropertyDescriptor pd : pds) {
                    if (pd.getWriteMethod() != null && pd.getReadMethod() != null) {
                        list.add(pd.getName());
                    }
                }
                if (list.size() > 0) {
                    attributeNames = list.toArray(new String[list.size()]);
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return attributeNames;
    }
}
