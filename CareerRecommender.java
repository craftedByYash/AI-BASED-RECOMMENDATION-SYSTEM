import java.io.*;
import java.util.*;

public class CareerRecommender {

    static class Candidate {
        String skills;
        String interests;
        String career;
        double score;

        Candidate(String skills, String interests, String career, double score) {
            this.skills = skills.toLowerCase();
            this.interests = interests.toLowerCase();
            this.career = career;
            this.score = score;
        }
    }

    public static void main(String[] args) throws Exception {

        List<Candidate> data = loadData("candidates.csv");

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your skills (comma separated):");
        String userSkills = sc.nextLine().toLowerCase();

        System.out.println("Enter your interests (comma separated):");
        String userInterests = sc.nextLine().toLowerCase();

        String bestCareer = "";
        double bestMatch = 0;

        for (Candidate c : data) {
            double skillMatch = similarity(userSkills, c.skills);
            double interestMatch = similarity(userInterests, c.interests);

            double finalScore = (skillMatch + interestMatch) / 2;

            if (finalScore > bestMatch) {
                bestMatch = finalScore;
                bestCareer = c.career;
            }
        }

        System.out.println("\n✅ Recommended Career: " + bestCareer);
        System.out.println("📊 Match Score: " + String.format("%.2f", bestMatch));
    }

    static List<Candidate> loadData(String file) throws Exception {
        List<Candidate> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            list.add(new Candidate(
                    parts[4],   // Skills
                    parts[5],   // Interests
                    parts[6],   // Recommended_Career
                    Double.parseDouble(parts[7])
            ));
        }
        br.close();
        return list;
    }

    static double similarity(String a, String b) {
        Set<String> setA = new HashSet<>(Arrays.asList(a.split("[,; ]+")));
        Set<String> setB = new HashSet<>(Arrays.asList(b.split("[,; ]+")));

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        return (double) intersection.size() /
               (Math.sqrt(setA.size()) * Math.sqrt(setB.size()) + 0.01);
    }
}
