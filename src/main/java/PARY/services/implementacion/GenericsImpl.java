package PARY.services.implementacion;

import PARY.services.contratos.IGenericsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GenericsImpl <E, D extends CrudRepository<E, Long>> implements IGenericsService<E> {
    @Autowired
    protected final D DAO;

    public GenericsImpl(D dao) {
        DAO = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        return (List<E>) DAO.findAll();
    }

    @Override
    @Transactional
    public E save(E dao) {
        return DAO.save(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public E findById(long id) {
        return DAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        System.out.println("si llamaa al metodo");
        DAO.deleteById(id);
    }

    @Override
    public void deleteAll() {
        DAO.deleteAll();
    }
}
