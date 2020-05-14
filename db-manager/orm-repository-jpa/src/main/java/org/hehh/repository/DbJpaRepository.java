package org.hehh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: HeHui
 * @date: 2020-05-04 21:02
 * @description: spring-data-jpa 资源库
 */
public interface DbJpaRepository<T,ID>  extends JpaRepository<T,ID>,Repository<T> {
}
