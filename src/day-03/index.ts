import { readFile } from 'fs/promises';

export async function day3(): Promise<void> {
  const memory = await readFile('memory.txt');
  const regExp = /mul\((\d{1,3}),(\d{1,3})\)/gm;
  const matches = memory.toString().matchAll(regExp);

  let result = 0;

  for (const match of matches) {
    const x = parseInt(match.groups?.[0] ?? '0');
    const y = parseInt(match.groups?.[1] ?? '0');
    result = result + (x * y);
  }

  console.log(`The result of adding up all of the multiplications is ${result}`);
}
