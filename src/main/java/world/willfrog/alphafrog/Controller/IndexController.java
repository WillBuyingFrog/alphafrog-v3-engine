package world.willfrog.alphafrog.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import world.willfrog.alphafrog.Service.Index.IndexFetchService;

@Controller
@RequestMapping("/index")
public class IndexController {


    @Autowired
    private IndexFetchService indexFetchService;


}