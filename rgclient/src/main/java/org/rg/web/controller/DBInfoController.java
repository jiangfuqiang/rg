package org.rg.web.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by jiang on 17/1/26.
 */
@Controller
@RequestMapping("/dbinfo")
public class DBInfoController extends BaseController {

    private static final Logger LOG = LogManager.getLogger(DBInfoController.class);

    @RequestMapping(value="/listTables")
    @ResponseBody
    public String listTables(HttpServletRequest request,HttpServletRequest response) {

        return "";

    }

}
