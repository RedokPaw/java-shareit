package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "from Booking as b where b.id = :bookerId and (:now between b.start and b.end) order by b.start desc")
    List<Booking> findCurrentBookingsByBookerId(int bookerId, LocalDateTime now);

    @Query(value = "from Booking as b where b.id = :bookerId and b.end < :now order by b.start desc")
    List<Booking> findPastBookingsByBookerId(int bookerId, LocalDateTime now);

    @Query(value = "from Booking as b where b.id = :bookerId and b.start > :now order by b.start desc")
    List<Booking> findFutureBookingsByBookerId(int bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    List<Booking> findByBookerIdOrderByStartDesc(int bookerId);

    @Query(value = "from Booking as b where b.item.owner.id = :ownerId and (:now between b.start and b.end) order by b.start desc")
    List<Booking> findCurrentBookingsByItemOwner(int ownerId, LocalDateTime now);

    @Query(value = "from Booking as b where b.item.owner.id = :ownerId and b.end < :now order by b.start desc")
    List<Booking> findPastBookingsByItemOwner(int ownerId, LocalDateTime now);

    @Query(value = "from Booking as b where b.item.owner.id = :ownerId and b.start > :now order by b.start desc")
    List<Booking> findFutureBookingsByItemOwner(int ownerId, LocalDateTime now);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    List<Booking> findByItem_Owner_IdOrderByStartDesc(int bookerId);

    @Query(value = "from Booking as b where b.booker.id = :bookerId and b.item.id = :itemId and b.status = 'APPROVED'" +
            "and b.end < :now ")
    List<Booking> findPastBookingsByBookerIdAndItem(int bookerId, int itemId, LocalDateTime now);

    @Query(value = "from Booking as b where b.item.id = :itemId and b.status = 'APPROVED'" +
            "and b.end < :now order by b.start desc")
    Booking findPreviousBookingByItem(int itemId, LocalDateTime now);

    @Query(value = "from Booking as b where b.item.id = :itemId and b.status = 'APPROVED'" +
            "and b.start > :now order by b.start asc")
    Booking findNextBookingByItem(int itemId, LocalDateTime now);

}
