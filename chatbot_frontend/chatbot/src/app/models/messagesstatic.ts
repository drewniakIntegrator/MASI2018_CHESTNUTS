import { Message } from './message';

export const MESSAGESSTATIC: Message[] = [
    new Message('', 'Hello! How can I help you?', false),
    new Message('', 'What category do you like?<br/>You can choose from the following or enter a diffrent one.',
        false, ['comedy', 'thriler', 'crime', 'action']),
    new Message('', 'Older or newer film?', false, ['<2000', '>=2000']),
    new Message('', 'Matrix, Matrix 2', false),
    new Message('', 'Just google bro. I am tired', false),
    new Message('', 'What did you say?', false),
];

