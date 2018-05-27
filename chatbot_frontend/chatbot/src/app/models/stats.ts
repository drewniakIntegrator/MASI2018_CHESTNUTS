import { AllStats } from "./allStats";
import { CurrentStatus } from "./currentStatus";

export class Stats {
    allConversationsStatistics: AllStats;
    conversationSummary: CurrentStatus;

    constructor() {
        this.allConversationsStatistics = new AllStats();
        this.conversationSummary = new CurrentStatus();
    }
}
