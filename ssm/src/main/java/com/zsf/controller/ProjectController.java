package com.zsf.controller;

import com.zsf.business.ProjectBusiness;
import com.zsf.business.RedisBusiness;
import com.zsf.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private RedisBusiness redisBusiness;

    @Autowired
    private ProjectBusiness projectBusiness;

    /**
     *  保存画面接口
     * @param svg
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody ResBody saveProject(@RequestBody SVGDto svg) {
        return redisBusiness.set(svg);
    }

    /**
     *  返回画面接口
     * @param svg
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public @ResponseBody ResBody getProject(@RequestBody SVGDto svg) {
        return redisBusiness.getValue(svg);
    }

    /**
     *  保存组合元素接口
      * @param group
     * @return
     */
    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    public @ResponseBody ResBody saveGroup(@RequestBody GroupedElement group) {
        return projectBusiness.saveGroupedElement(group);
    }

    /**
     *  保存组合元素接口
     * @param group
     * @return
     */
    @RequestMapping(value = "/getGroup", method = RequestMethod.POST)
    public @ResponseBody ResBody getGroup(@RequestBody GroupedElement group) {
        return redisBusiness.getGroupedElement(group);
    }
    /**
     *  获得集合接口
     * @param
     * @return
     */
    @RequestMapping(value = "/getProjectsCollection", method = RequestMethod.POST)
    public @ResponseBody ResBody getProjectsColletion() {
        return projectBusiness.getProjectsColletion();
    }

    /**
     *  添加集合元素
     * @param projectDto
     * @return
     */
    @RequestMapping(value = "/addProjectToCollection", method = RequestMethod.POST)
    public @ResponseBody ResBody addProjectToColletion(@RequestBody ProjectDto projectDto) {
        return projectBusiness.addProjectToColletion(projectDto);
    }

    public static void main(String[] args) {
        System.out.println("阿瑟东");
    }
}
