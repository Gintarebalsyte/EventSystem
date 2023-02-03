package lt.vu.mif.eventsystem.bl.user.repository.interfaces;

import lt.vu.mif.eventsystem.model.user.UserData;
import org.springframework.data.repository.CrudRepository;

public interface IUserDataRepository extends CrudRepository<UserData, Long> {
}

