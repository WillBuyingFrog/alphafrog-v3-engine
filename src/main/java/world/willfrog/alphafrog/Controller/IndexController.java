package world.willfrog.alphafrog.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import world.willfrog.alphafrog.Service.Index.IndexFetchService;

@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/get/info")
    public ResponseEntity<String> getIndexInfo() {

        return ResponseEntity.ok("Index Info");
    }

}