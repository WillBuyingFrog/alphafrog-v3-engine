package world.willfrog.alphafrog.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.springframework.http.ResponseEntity;

import world.willfrog.alphafrog.Entity.IndexDaily;
import world.willfrog.alphafrog.Service.IndexFetchService;

import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {


    @Autowired
    private IndexFetchService indexFetchService;


}