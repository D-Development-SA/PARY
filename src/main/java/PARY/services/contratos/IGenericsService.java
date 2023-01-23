package PARY.services.contratos;

import java.util.List;

public interface IGenericsService <E>{
    List<E> findAll();
    E save(E actividad);
    List<E> saveAll(List<E> actividad);
    E findById(long id);
    void deleteById(long id);
    void deleteAll();
}
