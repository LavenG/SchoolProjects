//Handles the eventsd for the filter button
function filterHandler(event){
  //prevent the default events from happening
  event.preventDefault();
  //Set up AJAX
  const req = new XMLHttpRequest();
  const director = document.getElementById('director').value;
  const url = 'http://localhost:3000/api/movies?director='+director;
  req.open('GET', url, true);
  //Listen on load
  req.addEventListener('load', function(){
    if(req.status >= 200 && req.status < 400) {
      //Parse in the data
      let data = JSON.parse(req.responseText);
      let moviesList = document.getElementById('movie-list');
      moviesList.innerHTML = '';
      data.forEach(function(movie){
        //For all the corresponding movies to the filer, create new elements and set up their text fields
        //Append the result to the table
        const tableRow = document.createElement('tr');
        const title = tableRow.appendChild(document.createElement('td'));
        const director = tableRow.appendChild(document.createElement('td'));
        const year = tableRow.appendChild(document.createElement('td'));
        title.textContent = movie.title;
        director.textContent = movie.director;
        year.textContent = movie.year;
        moviesList.appendChild(tableRow);
      });
    }
  });
  //send in the request
  req.send();
};

function addHandler(event){
  //prevent the default events from happening
  event.preventDefault();
  //Get the value of the user input
  const title = document.getElementById('movieTitle').value;
  const director = document.getElementById('movieDirector').value;
  const year = document.getElementById('movieYear').value;
  //Set up AJAX
  const req = new XMLHttpRequest();
  req.open('POST', 'http://localhost:3000/api/movies/create', true);
  req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  req.send(`title=${title}&director=${director}&year=${year}`);
  //Listen on load
  req.addEventListener('load', function(){
    document.getElementById('director').value = '';
    if(req.status >= 200 && req.status<400){
      //Create new elements for the added movie and append the result to the table
      const movieList = document.getElementById('movie-list');
      const tableRow = document.createElement('tr');
      const currTitle = tableRow.appendChild(document.createElement('td'));
      const currDirector = tableRow.appendChild(document.createElement('td'));
      const currYear = tableRow.appendChild(document.createElement('td'));
      currTitle.textContent = title;
      currDirector.textContent =director;
      currYear.textContent = year;
      movieList.appendChild(tableRow);
    }
  });
};

function main(){
  console.log('loaded');
  //Select the filter button from the document and handle its click
  const filterBtn = document.getElementById("filterBtn");
  filterBtn.addEventListener('click', filterHandler);
  //Select the add button from the document and handle its click
  const addBtn = document.getElementById("addBtn");
  addBtn.addEventListener('click', addHandler)
}
//Launch the client side javascript as soon as the document is loaded
document.addEventListener('DOMContentLoaded', main);
