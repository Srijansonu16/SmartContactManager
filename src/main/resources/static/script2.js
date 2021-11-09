function openNav() {
    document.getElementById("mySidebar").style.width = "250px";
    document.getElementById("main").style.marginLeft = "250px";
}

/* Set the width of the sidebar to 0 and the left margin of the page content to 0 */
function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
}

const search=()=>{
  let query=$("#search").val()

  if (query==""){
      $(".search-result").hide();

  }else {
      console.log(query);
      let url=`http://localhost:8080/Search_bar/${query}`;
      fetch(url).then((response) => {return response.json();}).then((data) => {console.log(data);
      let text =`<div class='list-group'>`;

          data.forEach((contact) => { text+=`<a href='/user/contact/${contact.id}' class="list-group-item list-group-action"> ${contact.name}</a>`});

          text += `</div>`;
          $(".search-result").html(text);
          $(".search-result").show();
      });

  }
};