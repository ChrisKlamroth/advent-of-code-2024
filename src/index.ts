import { day1 } from './day-01';
import { day2 } from './day-02';
import { day3 } from './day-03';

async function main(): Promise<void> {
  await day1();
  await day2();
  await day3();
}

main();
