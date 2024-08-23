package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwner_Id(int ownerId);

    @Query(value = "from Item as i where (i.description ilike :description or i.name ilike :description)" +
            " and i.available is true")
    List<Item> findAllByDescriptionContaining(String description);

    List<Item> findAllByRequest_Id(int id);
}
