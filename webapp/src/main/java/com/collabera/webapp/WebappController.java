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
    public String findallProducts(Model model)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<User>(headers);
        List<User> dataList = restTemplate.exchange("http://localhost:8085/users", HttpMethod.GET, entity, List.class).getBody();
        model.addAttribute("dataList", dataList);
        return "viewalluser";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model)
    {
        String name = user.getName();
        String email = user.getEmail();
        String position = user.getPosition();
        int id = user.getUser_Id();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);
        var myPassword = user.getPassword();
        var encodedPassword = encoder.encode(myPassword);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entitycheck = new HttpEntity<User>(headers);
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entitycheck, User.class).getBody();
        Optional<User> userdata = Optional.ofNullable(find);

        if (userdata.isPresent())
        {
            model.addAttribute("exist", "USERNAME ALREADY EXIST!");
            return "register";
        }

        else
        {
            user = new User(id, name, encodedPassword, email, position);
            HttpEntity<User> entity = new HttpEntity<User>(user);
            restTemplate.exchange("http://localhost:8085/register", HttpMethod.POST, entity, User.class).getBody();
            return "useradmin";
        }
    }

    @PostMapping("/userLogin")
    public String loginUser(@ModelAttribute("user") User user,@ModelAttribute("Movie")Movie movie, Model model, HttpSession session)
    {
        String name = user.getName();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<User>(headers);
        User find = restTemplate.exchange("http://localhost:8085/findbyname/"+name, HttpMethod.GET, entity, User.class).getBody();
        Optional<User> userdata = Optional.ofNullable(find);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);
        var myPassword = user.getPassword();
        var encodedPassword = encoder.encode(myPassword);
        var validPassword = encoder.matches(myPassword, encodedPassword);

        if(userdata.isPresent())
        {
            if (encoder.matches(user.getPassword(), userdata.get().getPassword()))
            {
                if (userdata.get().getPosition().equals("Admin"))
                {
                    session.setAttribute("username", user.getName());
                    model.addAttribute("message", "Welcome " + user.getName());
                    return "homeadmin";
                }
                else
                {
                    session.setAttribute("userid", user.getUser_Id());
                    session.setAttribute("username", user.getName());
                    HttpHeaders headers3 = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
                    List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
                    model.addAttribute("dataList", dataList);
                    return "home";
                }
            }
            else
            {
                model.addAttribute("invalid", "LOGIN INVALID!");
                return "index";
            }
        }
        else
        {
            model.addAttribute("invalid", "USER NOT EXIST!");
            return "index";
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
        System.out.println("-----------------------------------");
        System.out.println(dataid);


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
    public String addRate(@ModelAttribute("rating") Rating rating)
    {

        HttpEntity<Rating> entity = new HttpEntity<Rating>(rating);
        restTemplate.exchange("http://localhost:8082/addrating", HttpMethod.POST, entity, Rating.class).getBody();
        return "niceseller";
    }

    /*Fav*/
    @GetMapping("/fav")
    public String addFav(@RequestParam("id") int id, @ModelAttribute("favourite") Favourite favourite, Model model)
    {
        HttpEntity<Favourite> entity = new HttpEntity<Favourite>(favourite);
        restTemplate.exchange("http://localhost:8087/addFav", HttpMethod.POST, entity, Favourite.class).getBody();

        HttpHeaders headers3 = new HttpHeaders();
        headers3.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Movie> entity3 = new HttpEntity<Movie>(headers3);
        List<Movie> dataList = restTemplate.exchange("http://localhost:8083/movies", HttpMethod.GET, entity3, new ParameterizedTypeReference<List<Movie>>() {}).getBody();
        model.addAttribute("dataList", dataList);
        return "home";
    }

}
