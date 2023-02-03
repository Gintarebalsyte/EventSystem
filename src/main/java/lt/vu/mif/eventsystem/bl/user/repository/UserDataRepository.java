package lt.vu.mif.eventsystem.bl.user.repository;

import lt.vu.mif.eventsystem.model.user.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends CrudRepository<UserData, Long> {

}
