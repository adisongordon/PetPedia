async function populateBreeds(selection) {
    currUrl = window.location.origin;
    let breeds = null;
    try {
        breeds = await fetchBreeds(selection);
    } catch (e) {
        console.error(e);
        removeChildren(document.getElementById("breed"));
    }

    removeChildren(document.getElementById("breed"));
    let optAny = document.createElement("option");
    optAny.value = "Any";
    optAny.textContent = "Any";
    document.getElementById("breed").appendChild(optAny);
    for (let i = 0; i < breeds.length; i++) {
        let opt = document.createElement("option");
        opt.value = breeds[i].breed;
        opt.textContent = breeds[i].breed;
        document.getElementById("breed").appendChild(opt);
    }
}

function removeChildren(elem) {
    while (elem.firstChild) {
        elem.removeChild(elem.firstChild);
    }
}

async function fetchBreeds(selection) {
    let breedsObj = [];
    let breeds = await fetch(currUrl + "/api/pets/" + selection)
        .then((response) => {
            return response.json();
        })
        .then((jsonResp) => {
            breedsObj = jsonResp;
            return jsonResp;
        });
    return breedsObj;
}

async function petSearch() {
    let pets = {};
    let species = document.getElementById("species");
    let breedElem = document.getElementById("breed");
    let breedSelection = breedElem.options[breedElem.selectedIndex].text;
    let reqBody = {
                               "apikey": "chNw5wsa",
                               "objectType": "animals",
                               "objectAction": "publicSearch",
                               "search": {
                                   "resultStart": 0,
                                   "resultLimit": 6,
                                   "resultSort": "animalID",
                                   "resultOrder": "asc",
                                   "calcFoundRows": "Yes",
                                   "filters": [
                                       {
                                           "fieldName": "animalStatus",
                                           "operation": "equal",
                                           "criteria": "Available"
                                       },
                                       {
                                           "fieldName": "animalSpecies",
                                           "operation": "equal",
                                           "criteria": species.options[species.selectedIndex].text
                                       },
                                       {
                                           "fieldName": "animalName",
                                           "operation": "notcontain",
                                           "criteria": " "
                                       },
                                       {
                                           "fieldName": "animalLocationDistance",
                                           "operation": "radius",
                                           "criteria": document.getElementById("distance").value
                                       },
                                       {
                                           "fieldName": "animalLocation",
                                           "operation": "equals",
                                           "criteria": document.getElementById("zipcode").value
                                       }
                                   ],
                                   "fields": [
                                       "animalName", "animalOrgID", "animalLocationCitystate", "animalDescriptionPlain", "animalSpecies", "animalGeneralAge", "animalBreed", "animalPictures"
                                   ]
                               }
                           };
    if (breedSelection != "Any") {
        reqBody.search.filters.push({
            "fieldName": "animalBreed",
            "operation": "contains",
            "criteria": breedSelection
        })
    }
    await fetch("https://api.rescuegroups.org/http/v2.json",
        {
            method: "POST",
            body: JSON.stringify(reqBody),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            pets = data;
        });
    removeChildren(document.getElementsByClassName("cards")[0])
    await makePetCards(pets.data);
}

async function makePetCards(pets) {
    if (Object.keys(pets).length == 0) {
        var newCard = document.createElement("div");
        newCard.className = "card";

        var pxpy = document.createElement('div');
        pxpy.className = 'px-2 py-2';
        newCard.appendChild(pxpy);

        var petDiv = document.createElement('div');
        petDiv.className = "No results found :(";
        pxpy.appendChild(petDiv);

        var petHeader = document.createElement("h2");
        petHeader.textContent = "No results found :(";
        petDiv.appendChild(petHeader);

        var petDesc = document.createElement("p");
        petDesc.innerText = "We couldn't find any pets that match your search. Try again with a different search.."

        document.getElementsByClassName("cards")[0].appendChild(newCard);
        return;
    }
    for (let key in pets) {
        let petInfo = pets[key];
        var newCard = document.createElement("div");
        newCard.className = "card";

        var pxpy = document.createElement('div');
        pxpy.className = 'px-2 py-2';
        newCard.appendChild(pxpy);

        var petDiv = document.createElement('div');
        petDiv.className = petInfo.animalName;
        pxpy.appendChild(petDiv);

        var petImg = document.createElement("img");
        if (petInfo.animalPictures.length > 0) {
            petImg.src = petInfo.animalPictures[0].large.url;
            petImg.setAttribute("id", "petImg");
            petDiv.appendChild(petImg);
        }

        var petHeader = document.createElement("h2");
        petHeader.textContent = petInfo.animalName;
        petDiv.appendChild(petHeader);

        var petDesc = document.createElement("p");
        var parsed = new DOMParser().parseFromString(petInfo.animalDescriptionPlain, "text/html").documentElement.textContent;
        petDesc.innerText = parsed;
        petDiv.appendChild(petDesc);

        var petcol_xs = document.createElement('div');
        petcol_xs.className = 'col-xs-1';
        pxpy.appendChild(petcol_xs);

        let shelterInfo = await getShelterInfo(petInfo.animalOrgID);
        if (shelterInfo == null) {
            continue;
        }

        var petTable = document.createElement("table");
        petTable.className = "card-table";
        petTable.innerHTML = `
        <tr>
            <td>Breed</td>
            <td>${petInfo.animalBreed}</td>
        </tr>
        <tr>
            <td>Location</td>
            <td>${petInfo.animalLocationCitystate}</td>
        </tr>
        <tr>
            <td>Shelter</td>
            <td>${shelterInfo.orgName}</td>
        </tr>
        <tr>
            <td>Age</td>
            <td>${petInfo.animalGeneralAge}</td>
        </tr>
        `;
        petcol_xs.appendChild(petTable);

        var shelterButton = document.createElement("a");
        shelterButton.className = "btn";
        shelterButton.innerText = "Visit Shelter";
        shelterButton.href = shelterInfo.orgWebsiteUrl;
        petcol_xs.appendChild(shelterButton);

        document.getElementsByClassName("cards")[0].appendChild(newCard);
    }
}

async function getShelterInfo(shelterId) {
    let reqBody = {
        "apikey": "chNw5wsa",
        "objectType": "orgs",
        "objectAction": "publicSearch",
        "search": {
            "resultStart": 0,
            "resultLimit": 10,
            "resultSort": "orgID",
            "resultOrder": "asc",
            "calcFoundRows": "Yes",
            "filters": [
                {
                    "fieldName": "orgID",
                    "operation": "equal",
                    "criteria": shelterId
                }
            ],
            "fields": [
                "orgName", "orgCity", "orgWebsiteUrl"
            ]
        }
    }
    let shelterInfo = {};
    await fetch("https://api.rescuegroups.org/http/v2.json",
        {
            method: "POST",
            body: JSON.stringify(reqBody),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            shelterInfo = data;
        });
    for (let key in shelterInfo.data) {
        return shelterInfo.data[key];
    }
}