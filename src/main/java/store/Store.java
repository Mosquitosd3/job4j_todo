package store;

import model.Item;
import model.Users;

import java.util.List;

public interface Store {
    Item save(Item item);

    Users save(Users users);

    void update(Integer id);

    void delete(Integer id);

    List<Item> findAll();

    Item findByID(Integer id);

    Users findByEmail(String email);
}
