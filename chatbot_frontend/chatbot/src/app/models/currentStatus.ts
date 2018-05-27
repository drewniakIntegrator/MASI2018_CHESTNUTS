export class CurrentStatus {
    amountOfMisunderstoodQuestions: number;
    amountOfQuestions: number;
    effectivenessScore: number;
    numberOfQuestionToAmountOfProducts: any;
    usabilityScore: number;

    constructor() {
        this.amountOfMisunderstoodQuestions = 0;
        this.amountOfQuestions = 0;
        this.effectivenessScore = 0;
        this.usabilityScore = 0;
    }
}
