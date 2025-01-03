package world.willfrog.alphafrog.Entity.Common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "alphafrog_user",
        uniqueConstraints = {
                @UniqueConstraint( name = "unique_user_name", columnNames = "userName"),
                @UniqueConstraint( name = "unique_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "email")
    String email;

    @Column(name = "register_time")
    Long registerTime;

    @Column(name = "user_type")
    Integer userType;

    @Column(name = "user_level")
    Integer UserLevel;

    @Column(name = "credit")
    Integer credit;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registerTime=" + registerTime +
                ", userType=" + userType +
                ", UserLevel=" + UserLevel +
                ", credit=" + credit +
                '}';
    }

}
