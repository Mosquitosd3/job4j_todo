package store;

import model.Item;

import java.util.List;

public interface Store {
    void save(Item item);

    void update(Integer id);

    void delete(Integer id);

    List<Item> findAll();

    Item findByID(Integer id);
}
