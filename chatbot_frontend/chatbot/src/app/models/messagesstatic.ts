import { Message } from './message';
import { Item } from './item';

export const MESSAGESSTATIC: Message[] = [
    new Message('', 'Hello! How can I help you?', false),
    new Message('', 'What category do you like?<br/>You can choose from the following or enter a diffrent one.',
        false, ['comedy', 'thriler', 'crime', 'action']),
    new Message('', 'Older or newer film?', false, ['<2000', '>=2000']),
    new Message(
        '', 'I found perfect movie for you', false, [],
        [
            new Item('', 'Matrix',
                'https://images-na.ssl-images-amazon.com/images/I/91rXRq5977L._RI_SX300_.jpg',
                'https://www.amazon.com/Matrix-Keanu-Reeves/dp/B000GJPL1S/ref=sr_1_1?s=movies-tv&ie=UTF8&qid=1525513124&sr=1-1&keywords=matrix'
            ),
            new Item('', 'The Matrix Reloaded',
                'https://images-na.ssl-images-amazon.com/images/I/91uXCp8oxaL._RI_SX300_.jpg',
                'https://www.amazon.com/Matrix-Reloaded-Keanu-Reeves/dp/B001EBWIV8/ref=sr_1_2?s=movies-tv&ie=UTF8&qid=1525513124&sr=1-2&keywords=matrix'
            ),
        ]
    ),
    new Message('', 'Just google bro. I am tired', false),
    new Message('', 'What did you say?', false),
];

