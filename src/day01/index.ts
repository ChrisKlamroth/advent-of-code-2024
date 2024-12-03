import { createInterface, Interface } from 'readline';
import { createReadStream } from 'fs';

type LocationId = number;

export async function day1(): Promise<void> {
  const locationIdsLeft: LocationId[] = [];
  const locationIdsRight: LocationId[] = [];

  for await (const line of readFile()) {
    const [left, right] = parseLocationIds(line);
    locationIdsLeft.push(left);
    locationIdsRight.push(right);
  }

  const totalDistance = calculateTotalDistance(
    locationIdsLeft.toSorted(),
    locationIdsRight.toSorted(),
  );

  console.log(`The total distance is ${totalDistance}`);
}

function readFile(): Interface {
  const fileStream = createReadStream('location-ids.txt');
  return createInterface({
    input: fileStream,
    crlfDelay: Infinity,
  });
}

function parseLocationIds(line: string): [LocationId, LocationId] {
  const [locationIdLeft, locationIdRight] = line.split('\t').map(value => parseInt(value));
  return [locationIdLeft, locationIdRight];
}

function calculateTotalDistance(locationIdsLeft: LocationId[], locationIdsRight: LocationId[]): number {
  if (locationIdsLeft.length !== locationIdsRight.length) {
    throw new Error('Both location ID lists must have the same length');
  }

  let totalDistance = 0;

  for (let index = 0; index < locationIdsLeft.length; index++) {
    const distance = Math.abs(locationIdsLeft[index] - locationIdsRight[index]);
    totalDistance = totalDistance + distance;
  }

  return totalDistance;
}