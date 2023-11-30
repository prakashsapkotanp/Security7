package np.com.bloodlink.BloodlinkApi.repository;

import np.com.bloodlink.BloodlinkApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository <User,Long>{
    Optional<User> findByUsername(String Username);
}
