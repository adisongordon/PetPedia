function getApiData(species)
{
    breeds = [];
    breedCards = [];
    currUrl = window.location.origin;

    fetch(currUrl + "/api/pets/" + species)
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        breeds = data;

        for (let i = 0; i < breeds.length; i++)
        {
            //if (breeds[i].imgUrl == "") continue;
            breedCards.push(makeCard(breeds[i]))
            document.getElementsByClassName('container mt-5')[0].appendChild(breedCards[breedCards.length - 1]);
        }
    });

    return breedCards;
}

function makeCard(breedInfo) {
    var newCard = document.createElement('div');
    newCard.className = 'card';

    var pxpy = document.createElement('div');
    pxpy.className = 'px-2 py-2';
    newCard.appendChild(pxpy);

    var breedDiv = document.createElement('div');
    breedDiv.className = breedInfo.breed;
    pxpy.appendChild(breedDiv);

    var breedImg = document.createElement('img');
    breedImg.src = breedInfo.imgUrl;
    breedImg.setAttribute('id', 'breedImg');
    breedDiv.appendChild(breedImg);

    var breedHeader = document.createElement('h2');
    breedHeader.textContent = breedInfo.breed;
    breedDiv.appendChild(breedHeader);

    if (breedInfo.description != "") {
        var breedEmptyPara = document.createElement('p');
        breedEmptyPara.innerText = '';
        breedDiv.appendChild(breedEmptyPara);
    }

    var breedcol_xs = document.createElement('div');
    breedcol_xs.className = 'col-xs-1';
    pxpy.appendChild(breedcol_xs);

    var breedTable = document.createElement('table');
    breedTable.className = 'card-table';
    try {
        tableData = JSON.parse(breedInfo.tableInfo);
    } catch (error) {
        console.log(breedInfo.tableInfo);
    }
    breedTable.innerHTML = ""; 
    for (const key of Object.keys(tableData)) {
        breedTable.innerHTML += `
        <tr>
            <td>${key}</td>
            <td>${tableData[key]}</td>
        </tr>`
    }
    breedcol_xs.appendChild(breedTable);

    var breedButton = document.createElement('a');
    breedButton.className = 'btn';
    breedButton.innerText = 'Read More';
    breedButton.href = breedInfo.infoUrl;
    breedcol_xs.appendChild(breedButton);

    return newCard;
}

function searchBreeds(query) {
    for (let i = 0; i < breedCards.length; i++) {
        if (!breedCards[i].firstChild.firstChild.innerText
            .toLowerCase()
            .includes(query.toLowerCase())) {
            breedCards[i].style.display = 'none'
        }
        else {
            breedCards[i].style.display = ''
        }
    }
}

function clearSearch() {
    document.getElementById("breedSearch").value = '';
    searchBreeds('');
}