package ImageHoster.repository;

import ImageHoster.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class ImageRepository {

    //Get an instance of EntityManagerFactory from persistence unit with name as 'imageHoster'
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;


    //The method receives the Image object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void uploadImage(Image newImage) {
        //Complete the method
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();
            em.persist(newImage);
            transaction.commit();
        }
        catch (Exception e){
            System.out.println("error"+e);
            transaction.rollback();
        }
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch all the images from the database
    //Returns the list of all the images fetched from the database
    public List<Image> getAllImages() {
        //Complete the code
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = em.createQuery("SELECT i FROM Image i",Image.class);
        return typedQuery.getResultList();
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the image from the database with corresponding title
    //Returns the image in case the image is found in the database
    //Returns null if no image is found in the database
    public Image getImageByTitle(String title) {
        //Complete the code
        try {
            EntityManager em = emf.createEntityManager();
            TypedQuery<Image>typedQuery = em.createQuery("SELECT i FROM Image i WHERE i.title =:title",Image.class);
            typedQuery.setParameter("title",title);
            return typedQuery.getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the image from the database with corresponding id
    //Returns the image fetched from the database
    public Image getImage(Integer imageId) {
        //Complete the method
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Image>typedQuery = em.createQuery("SELECT i FROM Image i WHERE i.id=:imageId",Image.class);
            typedQuery.setParameter("imageId",imageId);
            Image foundImage = typedQuery.getSingleResult();
            return foundImage;
        }
        catch (NoResultException nre){
            return null;
        }
    }

    //The method receives the Image object to be updated in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void updateImage(Image updatedImage) {
        //Complete the method
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(updatedImage);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
        }
    }

    public void deleteImage(Image tobeDeletedImage){
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();
            em.remove(em.merge(tobeDeletedImage));
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
        }
    }
}
