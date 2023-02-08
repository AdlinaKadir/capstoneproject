package com.collabera.webapp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class WebappController {

    @Autowired
    private RestTemplate restTemplate;
    private WebappController imageService;

    @GetMapping("/") //to index page
    public String index(Model model)
    {
        User user = new User();
        model.addAttribute("user", user); //to send attribute user
        return "index";
    }

    /*USER*/

    @GetMapping("/useradmin") //to user page for admin
    public String useradmin()
    {
        return "useradmin";
    }
    @GetMapping("/register") //to register page for admin
    public String registerpage()
    {
        return "register";
    }

    @GetMapping("/home") //to home page for user
    public String home(Model model)
    {
        HttpHeaders headers = new HttpHeaders(); // create object
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); // set object as array
        HttpEntity<Movie> entity = new HttpEntity<Movie>(headers); // send object to entity
        List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity, List.class).getBody(); //entity to movie API
        model.addAttribute("dataList", dataList); // set dataList to model attribute
        return "home";
    }

    @GetMapping("/homeadmin") //to home for admin
    public String homeadmin()
    {
        return "homeadmin";
    }

    @GetMapping("/users") // to view all user page
    public String findallUser(Model model)
    {
        //fetch data of all user from user table via user-service
        HttpHeaders headers = new HttpHeaders(); // set object
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); //set object to array
        HttpEntity<User> entity = new HttpEntity<User>(headers); //set object to entity
        List<User> dataList = restTemplate.exchange("http://localhost:8085/users", HttpMethod.GET, entity, List.class).getBody(); //entity to user API
        model.addAttribute("dataList", dataList); // data is insert here to be use by thymeleaf in html
        return "viewalluser";
    }

    @PostMapping("/register") // after click register
    public String register(@ModelAttribute("user") User user, Model model)
    {
        String name = user.getName(); // get username input
        String email = user.getEmail(); // get email input
        String position = user.getPosition(); // get position input
        int id = user.getUser_Id(); // get id

        /*Encrypt the Password*/
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);
        var myPassword = user.getPassword(); // get password input
        var encodedPassword = encoder.encode(myPassword); //encrypt the password

        /*Find data with String name*/
        HttpHeaders headers = new HttpHeaders();// set object
        HttpEntity<User> entitycheck = new HttpEntity<User>(headers);// set object to entity
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entitycheck, User.class).getBody();//entity to user API to find data by name
        Optional<User> userdata = Optional.ofNullable(find);//optional object to hold result method find

        if (userdata.isPresent()) // if the data for String name exist
        {
            model.addAttribute("exist", "USERNAME ALREADY EXIST!"); // return message username exist
            return "register"; // redirect to register page
        }

        else
        {
            user = new User(id, name, encodedPassword, email, position); // get all the input data
            HttpEntity<User> entity = new HttpEntity<User>(user); //set data to entity
            restTemplate.exchange("http://localhost:8085/register", HttpMethod.POST, entity, User.class).getBody(); // add data to user table
            return "useradmin"; // redirect to useradmin page
        }
    }

    @PostMapping("/userLogin")
    public String loginUser(@ModelAttribute("user") User user,@ModelAttribute("Movie")Movie movie, Model model, HttpSession session)
    {
        // Find user by username
        String name = user.getName(); //get the username enter by user
        HttpHeaders headers = new HttpHeaders();// create object
        HttpEntity<User> entity = new HttpEntity<User>(headers); //set object to entity
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entity, User.class).getBody(); // find the username in database
        Optional<User> userdata = Optional.ofNullable(find); //optional object to hold result method find

        //encrypted password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);
        var myPassword = user.getPassword();
        var encodedPassword = encoder.encode(myPassword);
        var validPassword = encoder.matches(myPassword, encodedPassword); //check if password enter match with database

        if(userdata.isPresent()) //if username exist
        {
            if (encoder.matches(user.getPassword(), userdata.get().getPassword())) //if password enter match with database
            {
                if (userdata.get().getPosition().equals("Admin")) //check the position type of use
                {
                    session.setAttribute("username", user.getName()); //session with name
                    return "homeadmin"; //redirect to home for admin
                }
                else
                {
                    session.setAttribute("userid", userdata.get().getUser_Id()); //session for id
                    session.setAttribute("username", user.getName()); //session for username

                    //get the list of all movie in database
                    HttpHeaders headers3 = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
                    List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
                    model.addAttribute("dataList", dataList);
                    return "home"; // redirect to home for user
                }
            }
            else
            {
                model.addAttribute("invalid", "LOGIN INVALID!"); //return message login invalid
                return "index"; //redirect to index
            }
        }
        else
        {
            model.addAttribute("invalid", "USER NOT EXIST!"); //return message user not exist
            return "index"; //redirect to index
        }
    }

    /*MOVIE*/

    @GetMapping("/movieadmin") // to movie page for admin
    public String movieadmin()
    {
        return "movieadmin";
    }

    @GetMapping("/addmovie") // to add page for admin
    public String addmoviepage()
    {
        return "addmovie";
    }

    @PostMapping("/addmovie") //after admin click add movie
    public String admovie(@ModelAttribute("movie") Movie movie)
    {
        //add the data to movie table via movie-service
        HttpEntity<Movie> entity = new HttpEntity<Movie>(movie);
        restTemplate.exchange("http://localhost:8083/addMovie", HttpMethod.POST, entity, Movie.class).getBody(); //add Movie API
        return "movieadmin";
    }

    @GetMapping("/movieinfo") //to movie info page are user select a movie
    public String movieinfopage(@RequestParam("id") int id, Model model)
    {

        /*call the movie id requested*/
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Movie> entity = new HttpEntity<Movie>(headers);
        Movie dataList = restTemplate.exchange("http://localhost:8083/findbyid/"+id, HttpMethod.GET, entity, Movie.class).getBody();
        model.addAttribute("dataList", dataList);

        /*call similar id*/
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Recommend> entity2 = new HttpEntity<Recommend>(headers2);
        List<Recommend> dataid = restTemplate.exchange("http://localhost:8086/recommends/" + id, HttpMethod.GET, entity2, new ParameterizedTypeReference<List<Recommend>>() {}).getBody();

        /*call movie with similar id*/
        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataListmovie = new ArrayList<>();
        for (Recommend recommend : dataid) {
            int movieId = recommend.getMovieId();
            List<Movie> recommendedMovies = restTemplate.exchange("http://localhost:8083/movies/" + movieId, HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
            dataListmovie.addAll(recommendedMovies);
        }
        model.addAttribute("dataListmovie", dataListmovie);
        return "movieinfo";
    }

    /*Rating*/
    @PostMapping("/rate")
    public String addRate(@ModelAttribute("rating") Rating rating, Model model)
    {
        String timestamp = rating.getTimestamp(); //get timsestamp data
        int movieId = rating.getMovieId(); //get movieId data
        int userId = rating.getUserId(); //get userid data
        double movierating = rating.getMovierating(); //get movie rating
        int id = rating.getId(); //get id (auto increment)
        rating = new Rating(id,userId,movieId,movierating,timestamp); // assign the data to rating
        HttpEntity<Rating> entity = new HttpEntity<Rating>(rating); //assign rating to entity
        restTemplate.exchange("http://localhost:8084/addrating", HttpMethod.POST, entity, Rating.class).getBody(); //entity data to be added to db by rating-service

        /*List all movie to display at home*/
        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
        model.addAttribute("dataList", dataList);
        return "home";
    }

    /*Fav*/
    @GetMapping("/fav")
    public String addFav(@RequestParam("id") int id,@RequestParam("userId") int userId, @ModelAttribute("favourite") Favourite favourite, Model model)
    {
        // get id for favourie (it will auto increment)
        int idf = favourite.getId();

        favourite = new Favourite(idf, id, userId); // get all the data to be inserted in fav table
        HttpEntity<Favourite> entity = new HttpEntity<Favourite>(favourite); //assign the data to the entity
        restTemplate.exchange("http://localhost:8087/addFav", HttpMethod.POST, entity, Favourite.class).getBody(); //entity data send to fav-service to add in db

        /*find favourite data for the current user*/
        HttpHeaders headers = new HttpHeaders(); //create object
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); //object as array
        HttpEntity<Favourite> entity2 = new HttpEntity<Favourite>(headers); // object to entity
        List<Favourite> favList = restTemplate.exchange("http://localhost:8087/favs/"+userId, HttpMethod.GET, entity2, new ParameterizedTypeReference<List<Favourite>>(){}).getBody();

        /*find movie detail based on favourite data result*/
        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataListmovie = new ArrayList<>();
        for (Favourite favourite1 : favList) {
            int movieId = favourite1.getMovieId();
            List<Movie> favMovies = restTemplate.exchange("http://localhost:8083/movies/" + movieId, HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
            dataListmovie.addAll(favMovies);
        }
        model.addAttribute("dataList", dataListmovie);

        return "favourite";// redirect to favourite page
    }

    @GetMapping("/viewfavs") // page to view all favourites for the current user
    public String findallFav(Model model, HttpSession session)
    {
        /*find favourite data for the current user*/
        Integer userId = (Integer) session.getAttribute("userid");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Favourite> entity2 = new HttpEntity<Favourite>(headers);
        List<Favourite> favList = restTemplate.exchange("http://localhost:8087/favs/"+userId, HttpMethod.GET, entity2, new ParameterizedTypeReference<List<Favourite>>(){}).getBody();
        List<Integer> dataListid = new ArrayList<>();
        for (Favourite favourite : favList){
            int favid = favourite.getId();
            dataListid.add(favid);
        }
        model.addAttribute("dataid", dataListid);

        /*find movie detail based on favourite data result*/
        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataListmovie = new ArrayList<>();
        for (Favourite favourite1 : favList) {
            int movieId = favourite1.getMovieId();
            List<Movie> favMovies = restTemplate.exchange("http://localhost:8083/movies/" + movieId, HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
            dataListmovie.addAll(favMovies);
        }
        model.addAttribute("dataList", dataListmovie);
        return "favourite";
    }

    @PostMapping("/del")
    public String delfav(@RequestParam int id, HttpSession session,Model model)
    {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Integer> entity = new HttpEntity<Integer>(headers);
        String product = restTemplate.exchange("http://localhost:8087/delete/"+id, HttpMethod.DELETE, entity, String.class).getBody();

        Integer userId = (Integer) session.getAttribute("userid");
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Favourite> entity2 = new HttpEntity<Favourite>(headers2);
        List<Favourite> favList = restTemplate.exchange("http://localhost:8087/favs/"+userId, HttpMethod.GET, entity2, new ParameterizedTypeReference<List<Favourite>>(){}).getBody();

        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataListmovie = new ArrayList<>();
        for (Favourite favourite1 : favList) {
            int movieId = favourite1.getMovieId();
            List<Movie> favMovies = restTemplate.exchange("http://localhost:8083/movies/" + movieId, HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
            dataListmovie.addAll(favMovies);
        }
        model.addAttribute("dataList", dataListmovie);
        return "favourite";
    }

}
