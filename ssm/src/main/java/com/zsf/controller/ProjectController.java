package com.zsf.controller;

import com.zsf.domain.*;
import com.zsf.service.IProjectService;
import com.zsf.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IProjectService projectService;

    /**
     *  保存画面接口
     * @param svg
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody ResBody saveProject(@RequestBody SVGDto svg) {
        return redisService.set(svg);
    }

    /**
     *  返回画面接口
     * @param svg
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public @ResponseBody ResBody getProject(@RequestBody SVGDto svg) {
        return redisService.getValue(svg);
    }

    /**
     *  保存组合元素接口
      * @param group
     * @return
     */
    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    public @ResponseBody ResBody saveGroup(@RequestBody GroupedElement group) {
        return projectService.saveGroupedElement(group);
    }

    /**
     *  保存组合元素接口
     * @param group
     * @return
     */
    @RequestMapping(value = "/getGroup", method = RequestMethod.POST)
    public @ResponseBody ResBody getGroup(@RequestBody GroupedElement group) {
        return redisService.getGroupedElement(group);
    }
    /**
     *  获得集合接口
     * @param
     * @return
     */
    @RequestMapping(value = "/getProjectsCollection", method = RequestMethod.POST)
    public @ResponseBody ResBody getProjectsCollection() {
        return projectService.getProjectsCollection();
    }

    /**
     *  添加集合元素
     * @param projectDto
     * @return
     */
    @RequestMapping(value = "/addProjectToCollection", method = RequestMethod.POST)
    public @ResponseBody ResBody addProjectToCollection(@RequestBody ProjectDto projectDto) {
        return projectService.addProjectToCollection(projectDto);
    }

    /**
     *  添加集合元素
     * @param projectDto
     * @return
     */
    @RequestMapping(value = "/deleteSvg", method = RequestMethod.POST)
    public @ResponseBody ResBody deleteSvg(@RequestBody ProjectDto projectDto) {
        return projectService.deleteSvg(projectDto);
    }

    public static void main(String[] args) {
        System.out.println("阿瑟东");
    }
}
