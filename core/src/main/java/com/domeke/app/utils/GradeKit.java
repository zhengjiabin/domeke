package com.domeke.app.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.CodeTable;
import com.domeke.app.model.User;

/**
 * @author zhouying
 */
public class GradeKit {
    /**
     * 获得等级
     * 
     * @param user对象
     */
    public static Map<String, Object> getGrade(User user) {
        Map<String, Object> gradeMap = new HashMap<String, Object>();
        List<CodeTable> codetables = CodeKit.getList("grade");
        // 豆子
        Long peas = 0L;
        // 等级
        Long point = 0L;
        // 等级名
        String pointName = "";
        if (user.get("peas") != null || user.get("point") != null) {
            peas = user.get("peas");
            point = user.get("point");
        }
        for (int i = 1; i <= codetables.size(); i++) {
            int codekey = Integer.parseInt((String)codetables.get(i).get("codekey"));
            int startPoint = 0;
            if (i == 1) {
                startPoint = 0;
            } else {
                startPoint = Integer.parseInt((String)codetables.get(i - 1).get("codekey"));
            }
            if (point <= codekey && point >= startPoint) {
                pointName = codetables.get(i).getStr("codename");
                gradeMap.put("pointName", pointName);
                gradeMap.put("poin", point);
                gradeMap.put("peas", peas);
                break;
            }
        }
        return gradeMap;
    }
}
