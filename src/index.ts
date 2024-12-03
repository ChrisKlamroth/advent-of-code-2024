import { day1 } from './day01';
import { day2 } from './day02';
import { day3 } from './day03';

async function main(): Promise<void> {
  await day1();
  await day2();
  await day3();
}

main();
