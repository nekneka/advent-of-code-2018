use std::fs::File;
use std::io::{BufRead, BufReader, Result};
use std::collections::HashSet;

fn main() -> Result<()> {
    find_sum();
    repeated_freq();

    Ok(())
}

fn find_sum() -> Result<()> {
    let f = File::open("01-18-input")?;
    let mut sum = 0;

    for line in BufReader::new(f).lines() {
        sum += line?.parse::<i32>().unwrap();
    }

    println!("Sum: {}", sum);
    Ok(())    
}

fn repeated_freq() -> Result<()> {
    let mut seen_freqs = HashSet::new();
    let mut sum = 0;

    'find_rep: loop {
        let f = File::open("01-18-input")?;
        for line in BufReader::new(f).lines() {
            if seen_freqs.insert(sum) {
                sum += line?.parse::<i32>().unwrap();
            } else {
                println!("Repeated freaq: {}", sum);
                break 'find_rep;
            }
        }
    }

    Ok(())

}
