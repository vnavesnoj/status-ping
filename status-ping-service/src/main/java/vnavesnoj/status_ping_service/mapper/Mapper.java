package vnavesnoj.status_ping_service.mapper;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface Mapper<F, T> {

    T map(F object);

    T map(F from, T to);
}
