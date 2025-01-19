package com.jvb.demo.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jvb.demo.entity.Order;
import com.jvb.demo.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query(value = "SELECT tbl_order.* FROM tbl_order, account where account.id = tbl_order.account_id "
        +"AND account.email =:email AND (:status is NULL OR tbl_order.order_status =:status) "
        +"ORDER BY tbl_order.create_date DESC", nativeQuery = true)
    Page<Order> getByEmail(String email, String status, Pageable pageable);

    @Query(value = "SELECT * FROM tbl_order WHERE (:status is NULL OR tbl_order.order_status =:status) "
        +"AND (:fromDate is NULL OR cast(create_date as date) >= :fromDate) "
        +"AND (:toDate is NULL OR cast(create_date as date) <= :toDate)"
        +"ORDER BY create_date desc", nativeQuery = true)
    Page<Order> findByOrderStatus(String status, String fromDate, String toDate, Pageable pageable);

    @Query(value = "SELECT COALESCE(a.revenua, 0) as revenua, tbl_month.month "
        +"FROM (SELECT SUM(o.total_price) as revenua, month(o.create_date) as month "
            +"FROM tbl_order as o "
            +"WHERE year(o.create_date) = year(now()) "
            +"AND o.order_status = 'SHIPPED' GROUP BY month(o.create_date)) as a "
        +"RIGHT JOIN ("
            +"SELECT 1 as month UNION SELECT 2 as month UNION SELECT 3 as month UNION " 
            +"SELECT 4 as month UNION SELECT 5 as month UNION SELECT 6 as month UNION "
            +"SELECT 7 as month UNION SELECT 8 as month UNION SELECT 9 as month UNION "
            +"SELECT 10 as month UNION SELECT 11 as month UNION SELECT 12 as month) as tbl_month "
        +"ON a.month = tbl_month.month", nativeQuery = true)
    List<Object[]> getRevenuaByMonth();

    @Query(value = "SELECT COALESCE(a.revenua, 0) as revenua, tbl_dayofweek.dayofweek "
        +"FROM (SELECT SUM(o.total_price) as revenua, dayofweek(o.create_date) as dayofweek "
            +"FROM tbl_order as o "
            +"WHERE yearweek(o.create_date) = yearweek(now()) "
            +"AND o.order_status = 'SHIPPED' GROUP BY dayofweek(o.create_date)) as a "
        +"RIGHT JOIN ("
            +"SELECT 1 as dayofweek UNION SELECT 2 as dayofweek UNION SELECT 3 as dayofweek UNION "
            +"SELECT 4 as dayofweek UNION SELECT 5 as dayofweek UNION SELECT 6 as dayofweek UNION "
            +"SELECT 7 as dayofweek) as tbl_dayofweek "
        +"ON a.dayofweek = tbl_dayofweek.dayofweek", nativeQuery = true)
    List<Object[]> getRevenuaByDayofWeek();

    @Query(value = "SELECT count(id), o.order_status, month(o.create_date) as month "
        +"FROM tbl_order as o "
        +"WHERE year(o.create_date) = year(now()) "
        +"AND (o.order_status = 'SHIPPED' OR o.order_status = 'CANCELED' "
        +"OR o.order_status = 'REJECTED' OR o.order_status = 'SHIPMENT_FAILED') "
        +"GROUP BY o.order_status, month(o.create_date)", nativeQuery = true)
    List<Object[]> countOrderStatusByMonth();

    @Query(value = "SELECT count(id), o.order_status, dayofweek(o.create_date) as dayofweek "
        +"FROM tbl_order as o "
        +"WHERE yearweek(o.create_date) = yearweek(now()) "
        +"AND (o.order_status = 'SHIPPED' OR o.order_status = 'CANCELED' "
        +"OR o.order_status = 'REJECTED' OR o.order_status = 'SHIPMENT_FAILED') "
        +"GROUP BY o.order_status, dayofweek(o.create_date)", nativeQuery = true)
    List<Object[]> countOrderStatusByDayOfWeek();

    Integer countByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM tbl_order as o "
        +"WHERE DATE(o.create_date) = date(now()) "
        +"AND o.order_status = 'SHIPPED' ",nativeQuery = true)
    Double getTodayRevenua();

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM tbl_order "
        +"WHERE (date(canceled_date) = date(now()) or date(rejected_date) = date(now())) "
        +"AND is_refund = 1", nativeQuery = true)
    Double getTodayRefund();
}
