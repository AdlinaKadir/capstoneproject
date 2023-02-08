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

    @GetMapping("/")
    public String index(Model model)
    {
        User user = new User();
        model.addAttribute("user", user);
        return "index";
    }

    /*USER*/

    @GetMapping("/useradmin")
    public String useradmin()
    {
        return "useradmin";
    }
    @GetMapping("/register")
    public String registerpage()
    {
        return "register";
    }

    @GetMapping("/home")
    public String home(Model model)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity = new HttpEntity<Movie>(headers);
        List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity, List.class).getBody();
        model.addAttribute("dataList", dataList);
        return "home";
    }

    @GetMapping("/homeadmin")
    public String homeadmin()
    {
        return "homeadmin";
    }

    @GetMapping("/users")
    public String findallUser(Model model)
    {
        //fetch data of all user from user table via user-service
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<User>(headers);
        List<User> dataList = restTemplate.exchange("http://localhost:8085/users", HttpMethod.GET, entity, List.class).getBody();
        model.addAttribute("dataList", dataList); // data is insert here to be use by thymeleaf in html
        return "viewalluser";
    }

    @PostMapping("/register")
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
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entitycheck = new HttpEntity<User>(headers);
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entitycheck, User.class).getBody();
        Optional<User> userdata = Optional.ofNullable(find);

        if (userdata.isPresent()) // if the data for String name exist
        {
            model.addAttribute("exist", "USERNAME ALREADY EXIST!"); // return message username exist
            return "register"; // redirect to register page
        }

        else
        {
            user = new User(id, name, encodedPassword, email, position); // get all the input data
            HttpEntity<User> entity = new HttpEntity<User>(user);
            restTemplate.exchange("http://localhost:8085/register", HttpMethod.POST, entity, User.class).getBody(); // add data to user table
            return "useradmin"; // redirect to useradmin page
        }
    }

    @PostMapping("/userLogin")
    public String loginUser(@ModelAttribute("user") User user,@ModelAttribute("Movie")Movie movie, Model model, HttpSession session)
    {
        // Find user by username
        String name = user.getName(); //get the username enter by user
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<User>(headers);
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entity, User.class).getBody(); // find the username in database
        Optional<User> userdata = Optional.ofNullable(find);

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

    @GetMapping("/movieadmin")
    public String movieadmin()
    {
        return "movieadmin";
    }

    @GetMapping("/addmovie")
    public String addmoviepage()
    {
        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String admovie(@ModelAttribute("movie") Movie movie)
    {
        //add the data to movie table via movie-service
        HttpEntity<Movie> entity = new HttpEntity<Movie>(movie);
        restTemplate.exchange("http://localhost:8083/addMovie", HttpMethod.POST, entity, Movie.class).getBody();
        return "movieadmin";
    }

    @GetMapping("/movieinfo")
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

    // Java Controller
    @GetMapping("/records/{id}")
    public String showRecord(int id, Model model) {
        HttpHeaders headers2 = new HttpHeaders();
        HttpEntity<Movie> entity2 = new HttpEntity<Movie>(headers2);
        Movie list = restTemplate.exchange("http://localhost:8083/findbytmdbid/{id}"+id, HttpMethod.GET, entity2, Movie.class).getBody();
        model.addAttribute("recordId", list.getTmdbId());
        return "home";
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

        return "favourite";// redirect to favourite page
    }

}
