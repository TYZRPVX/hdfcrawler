package edu.hit.ehealth.main.vo;

import edu.hit.ehealth.main.util.Utils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class AbstractVOMD5 extends AbstractVO {


    private static final long serialVersionUID = 1L;
    private static Set<String> excludeFields = new HashSet<String>(
            Arrays.asList(new String[]{"primaryId", "crawlDate", "crawlPageUrl", "md5"})
    );
    private String md5;


    @Column(length = 32, unique = true)
    public String getMd5() {
        if (md5 == null || md5.isEmpty()) {
            return toMD5();
        }
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    public String toMD5() {
        StringBuffer values = new StringBuffer();
        for (String attr : getAttributeNames()) {
            if (excludeFields.contains(attr.toLowerCase())) {
                continue;
            }
            values.append(getAttributeValue(attr));
        }
        return Utils.strToMD5(values.toString());
    }

}
