package world.willfrog.alphafrog.Controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Service.Common.UserService;
import world.willfrog.alphafrog.Entity.Common.User;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    UserService userService;

    DateConvertUtils dateConvertUtils;

    public UserController(UserService userService, DateConvertUtils dateConvertUtils) {
        this.userService = userService;
        this.dateConvertUtils = dateConvertUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> map) {

        JSONObject res = new JSONObject();
        JSONObject loginInfo = new JSONObject(map);

        String temp = userService.login(loginInfo.getString("username"), loginInfo.getString("password"));

        if (temp.startsWith("ERR*")){
            res.put("message", temp);
            return ResponseEntity.status(401).body(res.toString());
        } else {
            res.put("message", temp);
            return ResponseEntity.ok(res.toString());
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, Object> map) {

        JSONObject res = new JSONObject();
        JSONObject logoutInfo = new JSONObject(map);

        int temp = userService.logout(logoutInfo.getString("token"));

        if (temp == 1){
            res.put("message", "ERR*Token not found");
            return ResponseEntity.status(401).body(res.toString());
        } else {
            res.put("message", "Logout successfully");
            return ResponseEntity.ok(res.toString());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> map) {

        JSONObject res = new JSONObject();
        JSONObject registerInfo = new JSONObject(map);

        User user = userService.createUser(registerInfo.getString("username"),
                registerInfo.getString("password"), registerInfo.getString("email"),
                System.currentTimeMillis()
                );

        if(user == null) {
            res.put("message", "Internal Error. Failed to create user.");
            return ResponseEntity.status(401).body(res.toString());
        }

        if (user.getUsername().startsWith("ERR*")){
            res.put("message", user.getUsername());
            return ResponseEntity.status(401).body(res.toString());
        } else {
            res.put("message", user.getUsername());
            return ResponseEntity.ok(res.toString());
        }

    }

}
