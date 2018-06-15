package com.zsf.controller;

import com.zsf.domain.SVGDto;
import com.zsf.domain.ResBody;
import com.zsf.util.errorcode.ErrorCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @RequestMapping(value = "/index")
    public String saveProject() {
        return "index";
    }
    private String SVG;

    // 保存画面
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody ResBody saveProject(@RequestBody SVGDto svg) {
        System.out.println(svg.getSvg());
        SVG = svg.getSvg();
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("abc");
        return body;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public @ResponseBody ResBody getProject(@RequestBody SVGDto svg) {
        ResBody body = new ResBody();
        body.setSuccess(true);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData(SVG);
        return body;
    }
    public void save(SVGDto svgDto) {

    }
}
