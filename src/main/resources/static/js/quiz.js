// Sample questions array
let questions = [
    {
        question: "What size of pet are you looking for?",
        options: ["Small (e.g., birds, fish, turtles)", "Medium (e.g., dogs, cats, rabbits)", "Large (e.g., horses)"],
    },
    {
        question: "What species are you interested in?",
        options: [],
    },
];

const speciesQuestions = {
    Cat: [
        {
            question: "Do you prefer a pet that's energetic and playful or calm and relaxed?",
            options: ["Energetic and playful", "Calm and relaxed"],
        },
        {
            question: "Would you like a pet that's affectionate or more independent?",
            options: ["Affectionate", "Independent"],
        },
        {
            question: "Are you looking for a pet that's highly social or more reserved and shy?",
            options: ["Social", "Shy and reserved"],
        },
    ],

    Dog: [
        {
            question: "Do you prefer a pet that's active and energetic or calm and relaxed?",
            options: ["Active and energetic", "Calm and relaxed"],
        },
        {
            question: "Would you like a pet that's affectionate or more independent?",
            options: ["Affectionate", "Independent"],
        },
        {
            question: "Are you looking for a pet that's highly social or more reserved and cautious?",
            options: ["Social", "Cautious"],
        },
        {
            question: "Would you like a pet that's obedient or dominant?",
            options: ["Obedient", "Dominant"],
        },
    ],

    Rabbit: [
        {
            question: "What size of rabbit are you looking for?",
            options: ["Dwarf(1 - 3 lb)", "Small(2.5 - 5 lb)", "Medium(5 - 8 lb)", "Large(8 - 10 lb)", "Giant(10 - 15 lb)"],
        },
    ],

    Fish: [
        {
            question: "What size aquarium are you planning to keep your fish in?",
            options: ["Small (10 gal)", "Medium (30 gal)", "Large (50 gal)", "Very large (200 gal)"],
        },
        {
            question: "How often would you prefer to care for your fish?",
            options: ["Daily", "Weekly", "Monthly"],
        },
        {
            question: "Are you looking for a fish that is aggressive or non-aggressive?",
            options: ["Aggressive", "Non-Aggressive", "Doesn't matter"],
        },
    ],

    Turtle: [
        {
            question: "What type of environment do you prefer for your pet?",
            options: ["Terrestrial", "Semi-aquatic", "Aquatic"],
        },
        {
            question: "How much effort are you willing to put into caring for your pet?",
            options: ["Low Maintenance", "Moderate Maintenance", "High Maintenance"],
        },
    ],

    Bird: [
        {
            question: "What noise level would you prefer from your bird?",
            options: ["Quiet", "Loud"],
        },
        {
            question: "What lifespan range are you looking for in a bird?",
            options: ["Under 15 years", "Under 40 years", "Under 80 years"],
        },
    ],

    Horse: [
        {
            question: "What level of experience do you have with horses?",
            options: ["Beginner", "Moderate experience", "Experienced"],
        },
    ]
};

let currentQuestionIndex = 0;
let userAnswers = [];
let pets = [];
let species;
let speciesSelected = false;

const speciesOptions = {
    Small: ["Bird", "Fish", "Turtle"],
    Medium: ["Dog", "Cat", "Rabbit"],
    Large: ["Horse"]
}

function startQuiz() {
    document.getElementById('quiz-cover').style.display = 'none';
    document.getElementById('quiz-container').style.display = 'flex';
    loadQuestion(currentQuestionIndex);
}

function nextQuestion() {
    const selectedOption = document.querySelector('input[name="answer"]:checked')
    if (selectedOption) {
        userAnswers.push(selectedOption.value);
        if (!speciesSelected) {
            if (currentQuestionIndex === 0) {
                const size = selectedOption.value.split(" ")[0];
                questions[1].options = speciesOptions[size];
                currentQuestionIndex++;
            } else if (currentQuestionIndex === 1) {
                species = selectedOption.value.toLowerCase();
                pets = getPets();
                updateQuestionsForSpecies(selectedOption.value);
                currentQuestionIndex = 0;
                speciesSelected = true;
            }
        } else {
            currentQuestionIndex++;
        }

        if (currentQuestionIndex < questions.length) {
            loadQuestion(currentQuestionIndex);
        } else {
            showResults();
        }
    } else {
        alert("Please select an answer before proceeding.")
    }

}

function updateQuestionsForSpecies(option) {
    questions = speciesQuestions[option] || [];
}

function loadQuestion(index) {
    const questionContainer = document.querySelector('.question');
    const optionsContainer = document.querySelector('.options');
    questionContainer.textContent = questions[index].question;
    optionsContainer.innerHTML = '';

    questions[index].options.forEach((option, i) => {
        const li = document.createElement('li');
        li.classList.add('option');
        li.innerHTML = `<input type="radio" name="answer" id="option${i}" value="${option}">
                            <label for="option${i}">${option}</label>`;
        optionsContainer.appendChild(li);
    });
}

function showResults() {
    document.getElementById('quiz-container').style.display = 'none';
    document.getElementById('quiz-result').style.display = 'flex';

    const resultList = document.getElementById('result-list');
    resultList.innerHTML = '';

    const matchingPets = findMatchingPets(userAnswers);
    if (matchingPets.length > 0) {
        matchingPets.forEach(pet => {
            const li = document.createElement('li');
            li.classList.add('result-item');

            // Create a container for each pet's information
            const petCard = document.createElement('div');
            petCard.classList.add('pet-card');

            // Add the pet's image
            const petImage = document.createElement('img');
            petImage.src = pet.imgUrl || 'https://via.placeholder.com/150'; // Default image if none exists
            petImage.alt = `${pet.breed} image`;
            petImage.classList.add('pet-image');

            // Add breed, species, and description
            const petInfo = document.createElement('div');
            petInfo.classList.add('pet-info');

            const breedInfo = document.createElement('h5');
            breedInfo.textContent = `${pet.breed}`;

            let descriptionInfo;

            if (species === "cat") {
                descriptionInfo = document.createElement('p');
                descriptionInfo.textContent = pet.description;
            } else {
                var breedTable = document.createElement('table');
                tableData = JSON.parse(pet.tableInfo);
                breedTable.innerHTML = "";
                for (const key of Object.keys(tableData)) {
                    if (tableData[key] === null || tableData[key] === "") {
                        continue;
                    }
                    breedTable.innerHTML += `
                        <tr>
                            <td>${key}</td>
                            <td>${tableData[key]}</td>
                        </tr>`
                }
            }

            let scoreInfo;
            if ("score" in pet) {
                scoreInfo = document.createElement('p');
                scoreInfo.textContent = `Match Score: ${pet.score}`;
            }

            // Append all elements to the pet card
            petInfo.appendChild(breedInfo);
            if (species === "cat") {
                petInfo.appendChild(descriptionInfo);
            } else {
                petInfo.appendChild(breedTable);
            }
            if ("score" in pet) {
                petInfo.appendChild(scoreInfo);
            }
            petCard.appendChild(petImage);
            petCard.appendChild(petInfo);

            // Append the pet card to the result list
            li.appendChild(petCard);
            resultList.appendChild(li);
        });
    } else {
        resultList.innerHTML = "<li>No exact matches found.Try adjusting your preferences!</li>";
    }
}

function restartQuiz() {
    location.reload();
}

function getPets() {
    currUrl = window.location.origin;
    breeds = [];
    pet = [];

    fetch(currUrl + "/api/pets/" + species)
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            breeds = data;
            for (let i = 0; i < breeds.length; i++) {
                pet.push(breeds[i]);
            }
        });

    return pet;
}

function findMatchingPets(answers) {

    const dogSynonymGroups = {
        Active: ["active", "energetic", "playful", "curious", "fun-loving", "adventurous", "excitable", "keen"],
        Calm: ["calm", "gentle", "composed", "steady", "even tempered", "tolerant", "easygoing"],
        Affectionate: ["affectionate", "loving", "sweet-tempered", "kind", "gentle", "companionable"],
        Independent: ["independent", "aloof", "dignified", "reserved", "self-assured", "proud"],
        Social: ["sociable", "friendly", "outgoing", "joyful", "cheerful", "good-natured", "happy"],
        Cautious: ["cautious", "reserved", "sensitive", "adaptable"],
        Obedient: ["obedient", "trainable", "responsive", "dutiful", "reliable", "responsible", "docile"],
        Dominant: ["dominant", "protective", "territorial", "fierce"],
    };

    const catSynonymGroups = {
        Active: ["active", "energetic", "playful", "curious", "lively", "agile", "fun-loving"],
        Calm: ["calm", "relaxed", "patient", "quiet", "peaceful", "gentle"],
        Affectionate: ["affectionate", "sweet", "warm", "dependent", "devoted", "loyal"],
        Independent: ["independent", "sensible"],
        Social: ["social", "interactive", "easy going", "friendly", "highly interactive", "outgoing", "talkative", "expressive"],
        Shy: ["shy", "reserved", "sensitive", "quiet"],
    };


    const matchedPets = [];

    if (species === "dog") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails;
            try {
                petDetails = JSON.parse(pet.tableInfo);
            } catch (error) {
                return;
            }
            let petTemperament = petDetails.Temperament.split(", ").map(t => t.trim().toLowerCase());
            if (answers.includes("Active and energetic")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Active.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Calm and relaxed")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Calm.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (answers.includes("Affectionate")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Affectionate.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Independent")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Independent.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (answers.includes("Social")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Social.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Cautious")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Cautious.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (answers.includes("Obedient")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Obedient.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Dominant")) {
                petTemperament.forEach(temperament => {
                    if (dogSynonymGroups.Dominant.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (score >= 4) {
                matchedPets.push({...pet, score})
            }
        });
    } else if (species === "cat") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails = JSON.parse(pet.tableInfo);
            let petTemperament = petDetails.Temperament.split(", ").map(t => t.trim().toLowerCase());
            if (answers.includes("Energetic and playful")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Active.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Calm and relaxed")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Calm.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (answers.includes("Affectionate")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Affectionate.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Independent")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Independent.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (answers.includes("Social")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Social.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            } else if (answers.includes("Shy and reserved")) {
                petTemperament.forEach(temperament => {
                    if (catSynonymGroups.Shy.includes(temperament)) {
                        score += 1;  // Increment score if a match is found
                    }
                });
            }

            if (score >= 4) {
                matchedPets.push({...pet, score})
            }
        });
    } else if (species === "rabbit") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails = JSON.parse(pet.tableInfo);
            let petSize = petDetails.Size;
            if (answers.includes("Dwarf(1 - 3 lb)") && petSize.includes("Dwarf")) {
                score += 1;
            } else if (answers.includes("Small(2.5 - 5 lb)") && petSize.includes("Small")) {
                score += 1;
            } else if (answers.includes("Medium(5 - 8 lb)") && petSize.includes("Medium")) {
                score += 1;
            } else if (answers.includes("Large(8 - 10 lb)") && petSize.includes("Large")) {
                score += 1;
            } else if (answers.includes("Giant(10 - 15 lb)") && petSize.includes("Giant")) {
                score += 1;
            }
            if (score >= 1) {
                matchedPets.push({...pet})
            }
        });
    } else if (species === "fish") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails = JSON.parse(pet.tableInfo);
            let petSize = petDetails["Aquarium Size"];
            let petTemperament = petDetails.Temperament;
            let petCare = petDetails["Difficulty Of Care"];

            if (answers.includes("Small (10 gal)") && petSize?.includes("Small")) {
                score += 1;
            } else if (answers.includes("Medium (30 gal)") && petSize?.includes("Medium")) {
                score += 1;
            } else if (answers.includes("Large (50 gal)") && petSize?.includes("Large")) {
                score += 1;
            } else if (answers.includes("Very large (200 gal)") && petSize?.includes("Very large")) {
                score += 1;
            }

            if (answers.includes("Daily") && petCare?.includes("Daily")) {
                score += 1;
            } else if (answers.includes("Weekly") && petCare?.includes("Weekly")) {
                score += 1;
            } else if (answers.includes("Monthly") && petCare?.includes("Monthly")) {
                score += 1;
            }

            if (answers.includes("Aggressive") && petTemperament.includes("Aggressive")) {
                score += 1;
            } else if (answers.includes("Non-Aggressive") && !petTemperament.includes("Aggressive")) {
                score += 1;
            } else if (answers.includes("Doesn't matter")) {
                score += 1;
            }

            if (score >= 3) {
                matchedPets.push({...pet})
            }
        });
    } else if (species === "turtle") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails;
            try {
                petDetails = JSON.parse(pet.tableInfo);
            } catch (error) {
                return;
            }
            let petType = petDetails.Type;
            let petCare = petDetails["Difficulty Of Care"];

            if (answers.includes("Terrestrial") && petType.includes("Terrestrial")) {
                score += 1;
            } else if (answers.includes("Semi-aquatic") && petType.includes("Semi")) {
                score += 1;
            } else if (answers.includes("Aquatic") && petType.includes("Aquatic")) {
                score += 1;
            }

            if (answers.includes("Low Maintenance") && petCare?.includes("Low")) {
                score += 1;
            } else if (answers.includes("Moderate Maintenance") && !petCare?.includes("Low") && !petCare?.includes("High")) {
                score += 1;
            } else if (answers.includes("High Maintenance") && petCare?.includes("High")) {
                score += 1;
            }

            if (score >= 2) {
                matchedPets.push({...pet})
            }
        });
    } else if (species === "bird") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails;
            try {
                petDetails = JSON.parse(pet.tableInfo);
            } catch (error) {
                return;
            }
            let petLife = petDetails.Lifespan;
            let maxLife = 0;
            let petSound = petDetails.Sounds;

            if (petSound.includes("Quiet") || petSound.includes("quiet")) {
                if (answers.includes("Quiet")) {
                    score += 1;
                }
            } else {
                if (answers.includes("Loud")) {
                    score += 1;
                }
            }

            if (petLife) {
                const lifespan = petLife.match(/\d+/g);
                if (lifespan && lifespan.length > 0) {
                    maxLife = parseInt(lifespan[lifespan.length - 1]);
                }
            }

            if (maxLife > 0 && maxLife <= 15) {
                if (answers.includes("Under 15 years")) {
                    score += 1;
                }
            } else if (maxLife > 15 && maxLife <= 40) {
                if (answers.includes("Under 40 years")) {
                    score += 1;
                }
            } else if (maxLife > 40 && maxLife <= 80) {
                if (answers.includes("Under 80 years")) {
                    score += 1;
                }
            }

            if (score >= 2) {
                matchedPets.push({...pet})
            }
        });
    } else if (species === "horse") {
        pets.forEach(pet => {
            let score = 0;
            let petDetails;
            try {
                petDetails = JSON.parse(pet.tableInfo);
            } catch (error) {
                return;
            }
            let petBest = petDetails["Best Suited For"];

            if (petBest?.includes("children") || petBest?.includes("beginners") || petBest?.includes("young")) {
                if (answers.includes("Beginner")) {
                    score += 1;
                }
            } else if (petBest?.includes("Experienced") || petBest?.includes("large")) {
                if (answers.includes("Experienced")) {
                    score += 1;
                }
            } else {
                if (answers.includes("Moderate experience")) {
                    score += 1;
                }
            }

            if (score >= 1) {
                matchedPets.push({...pet})
            }
        });
    }


    matchedPets.sort((a, b) => b.score - a.score);
    return matchedPets;
}