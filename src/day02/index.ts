import { createInterface, Interface } from 'readline';
import { createReadStream } from 'fs';

type Level = number;
type Report = Level[];

export async function day2(): Promise<void> {
  const safeReports: Report[] = [];

  for await (const line of readFile()) {
    const report = parseReport(line);
    if (isReportSafe1(report)) {
      safeReports.push(report);
    }
  }

  console.log(`${safeReports.length} are safe`);
}

function readFile(): Interface {
  const fileStream = createReadStream('reports.txt');
  return createInterface({
    input: fileStream,
    crlfDelay: Infinity,
  });
}

function parseReport(line: string): Report {
  return line.split(' ').map(value => parseInt(value));
}

// ----- Option 1 -----

function isReportSafe1(report: Report): boolean {
  return areIncreasingLevelsValid(report) || areDecreasingLevelsValid(report);
}

function areIncreasingLevelsValid(report: Report): boolean {
  for (let index = 0; index < report.length - 1; index++) {
    // Levels are increasing.
    if (report[index + 1] <= report[index]) {
      return false;
    }
    // Level difference is at most 3.
    if (report[index + 1] - report[index] > 3) {
      return false;
    }
  }
  return true;
}

function areDecreasingLevelsValid(report: Report): boolean {
  for (let index = 0; index < report.length - 1; index = index++) {
    // Levels are decreasing.
    if (report[index + 1] >= report[index]) {
      return false;
    }
    // Level difference is at most 3.
    if (report[index] - report[index + 1] > 3) {
      return false;
    }
  }
  return true;
}

// ----- Option 2 -----

function isReportSafe2(report: Report): boolean {
  return (areLevelsIncreasing(report) && areLevelDifferencesValid(report))
    || (areLevelsDecreasing(report) && areLevelDifferencesValid(report));
}

function areLevelsIncreasing(report: Report): boolean {
  for (let index = 0; index < report.length - 1; index = index++) {
    if (report[index + 1] <= report[index]) {
      return false;
    }
  }
  return true;
}

function areLevelsDecreasing(report: Report): boolean {
  for (let index = 0; index < report.length - 1; index = index++) {
    if (report[index + 1] >= report[index]) {
      return false;
    }
  }
  return true;
}

function areLevelDifferencesValid(report: Report): boolean {
  for (let index = 0; index < report.length - 1; index = index++) {
    const difference = Math.abs(report[index + 1] - report[index]);
    if (difference < 1 || 3 < difference) {
      return false;
    }
  }
  return true;
}
