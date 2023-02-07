<script>
  document.addEventListener("DOMContentLoaded", function() {
    var movies = dataList;
    var poster;
    const IMG_PATH = 'https://image.tmdb.org/t/p/w300';
    const movieMain = document.getElementById('movieMain');

    movieMain.innerHTML = '';
    movies.forEach((movie) => {
      console.log(movie);
      let url = `https://api.themoviedb.org/3/movie/${movie.tmdbId}?api_key=3fd2be6f0c70a2a598f084ddfb75487c&language=en-US`;
      console.log('JavaScript is running');
      fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
          const movieCard = document.createElement('div');

          if (movie.poster != null) {
            poster = movie.poster;
          } else if (data.poster_path == null) {
            poster = 'https://bookstore.ams.org/images/not_found.png';
          } else {
            poster = IMG_PATH + data.poster_path;
          }
          console.log(poster);
          movieCard.classList.add('col');
          movieCard.innerHTML = `
            <div class="card h-100" style="background-color: #e4e4e4">
              <a href="/movieinfo?id=${movie.movieId}">
                <div id="movieCard">
                  <img src="${poster}" class="card-img-top" alt="alt" />
                </div>
              </a>
              <div class="card-body">
                <h5 class="card-title">${movie.title}</h5>
              </div>
            </div>`;
          movieMain.appendChild(movieCard);
        });
    });
  });
</script>
