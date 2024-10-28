package world.willfrog.alphafrog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("world.willfrog.alphafrog.Dao")
public class AlphafrogApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlphafrogApplication.class, args);
    }

}
