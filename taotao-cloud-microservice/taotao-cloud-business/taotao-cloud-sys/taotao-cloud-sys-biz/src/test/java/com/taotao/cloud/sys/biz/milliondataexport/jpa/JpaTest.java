package com.taotao.cloud.sys.biz.milliondataexport.jpa;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

import jakarta.persistence.QueryHint;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * JpaTest
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class JpaTest {

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query(value = "select t from Todo t")
    Stream<Todo> streamAll();

    @RequestMapping(value = "/todos.csv", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public void exportTodosCSV( HttpServletResponse response ) {
        response.addHeader("Content-Type", "application/csv");
        response.addHeader("Content-Disposition", "attachment; filename=todos.csv");
        response.setCharacterEncoding("UTF-8");
        try (Stream<Todo> todoStream = todoRepository.streamAll()) {
            PrintWriter out = response.getWriter();
            todoStream.forEach(rethrowConsumer(todo -> {
                String line = todoToCSV(todo);
                out.write(line);
                out.write("\n");
                entityManager.detach(todo);
            }));
            out.flush();
        } catch (IOException e) {
            log.info("Exception occurred " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while exporting results", e);
        }
    }
}
