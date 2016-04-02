package edu.hit.ehealth.test;

import edu.hit.ehealth.main.util.RegexUtils;

public class TestRe {
	public static void main(String[] args) {

		
		String line = "            <td width=\"50%\"><span>疾病：</span><a href=\"http://www.haodf.com/jibing/kuozhangxingxinjibing.htm\" target=\"_blank\" class=\"orange\" style=\"text-decoration: none;\">扩张型心肌病</a>			</td>";
		String disease = RegexUtils.regexFind(".+none;\">(\\S+)</a>", line);
		System.out.println(disease);
		String line1 = "            <td width=\"50%\"><span>疾病：</span>急性心肌心包炎			</td>";
		String disease1 = RegexUtils.regexFind(".+</span>(.*)</td>", line1);	
		System.out.println(disease1);
	}

}
