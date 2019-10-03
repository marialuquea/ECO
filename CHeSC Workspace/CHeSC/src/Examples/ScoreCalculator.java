package Examples;

import java.util.ArrayList;
import java.util.Collections;

/*
 * @author Dr Matthew Hyde
 * ASAP Research Group, School of Computer Science, University of Nottingham
 * 
 * This program is for use when developing Hyper-Heuristics for the Cross-Domain Heuristic Search Challenge 2011.
 * It allows the comparison of your Hyper-Heuristic's scores against some Hyper-Heuristics that have been
 * developed by us during the preparation and testing of the competition software.
 * 
 * Once the objective function values found by your hyper-heuristic (each over 10 minutes) have been entered, the program outputs the relative scores
 * 
 * disclaimer:
 * -We provide this test program ONLY as a rough benchmark.
 * -We make no guarantees about the quality (or lack thereof) of these hyper-heuristics, but we believe they are of average quality. 
 * -Success against these hyper-heuristics will not necessarily translate into success in the competition, as that depends on the quality of the other competitors' hyper-heuristics.
 * 
 * please report any bugs to Matthew Hyde at mvh@cs.nott.ac.uk
 */

public class ScoreCalculator {

	//please replace the ten '0's in each problem domain with your scores for each of the 10 training instances
	static final double[] YourSAT = {100, 99, 300, 1, 555, 111, 34, 12, 11, 12};
	static final double[] YourBinPacking = {10, 20, 30, 40, 50, 0, 10, 20, 30, 40};
	static final double[] YourPersonnelScheduling = {100, 100, 200, 3000, 40, 50, 60, 80, 900, 10};
	static final double[] YourFlowshop = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};

	/*the competition will use 5 instances, and we provide 10 training instances so choose the 
	 * instances from the training set that you would like to use for this 'mock' competition. */
	static final int number_of_instances = 5;
	static final int[] chosen_instances_sat = {0, 1, 2, 3, 4};
	static final int[] chosen_instances_binpacking = {2, 3, 4, 5, 6};
	static final int[] chosen_instances_personnelscheduling = {7, 1, 3, 4, 6};
	static final int[] chosen_instances_flowshop = {5, 6, 7, 8, 9};

	static final int[][] YourInstances = {chosen_instances_sat, chosen_instances_binpacking, chosen_instances_personnelscheduling, chosen_instances_flowshop};
	static final double[][] YourResults = {YourSAT, YourBinPacking, YourPersonnelScheduling, YourFlowshop};

	public static void main (String[] argv) {

		//this adds your results (input above) to the end of the existing score arrays
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < 10; i++) {
				results[d][i][8] = YourResults[d][i];
			}}

		int hyperheuristics = 9;
		double[] scores = new double[hyperheuristics];
		double[] basescores = {10,8,6,5,4,3,2,1};
		for (int domain = 0; domain < 4; domain++) {
			double[] domainscores = new double[hyperheuristics];
			for (int i = 0; i < number_of_instances; i++) {
				int instance = YourInstances[domain][i];
				double[] res = results[domain][instance];
				ArrayList<Score> al = new ArrayList<Score>();
				for (int s = 0; s < res.length; s++) {
					Score obj = new Score(s, res[s]);
					al.add(obj);
				}
				Collections.sort(al); 
				double lastscore = Double.POSITIVE_INFINITY;
				int scoreindex = 0;
				int sco = 0;
				double tieaverage = 0;
				int tienumber = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();
				while (scoreindex < basescores.length) {
					Score test1 = al.get(sco);
					if (test1.score != lastscore) {
						double average = tieaverage / list.size();
						for (int f = 0; f < list.size(); f++) {
							scores[list.get(f)] += average;
							domainscores[list.get(f)] += average;
						}
						tienumber = 0;
						tieaverage = 0;
						list = new ArrayList<Integer>();
						list.add(test1.num);
						tienumber++;
						tieaverage += basescores[scoreindex];
					} else {
						list.add(test1.num);
						tienumber++;
						tieaverage += basescores[scoreindex];
					}
					lastscore = test1.score;
					sco++;
					scoreindex++;
				}
			}
			String d = "";
			switch (domain) {
			case 0: d = "SAT";break;
			case 1: d = "Bin Packing";break;
			case 2: d = "Personnel Scheduling";break;
			case 3: d = "Flow Shop";break;}
			System.out.println(d);
			for (int g = 0; g < domainscores.length; g++) {
				if (g == 8) {
					System.out.println("YourHH" + " " + domainscores[g]);
				} else {
					System.out.println("TestHH"+ (g+1) + " " + domainscores[g]);
				}
			}System.out.println();
		}
		System.out.println("Overall Total ");
		for (int g = 0; g < scores.length; g++) {
			if (g == 8) {
				System.out.println("YourHH" + " " + scores[g]);
			} else {
				System.out.println("TestHH"+ (g+1) + " " + scores[g]);
			}
		}
	}

	static double[] SAT0 = {70.0, 60.0, 14.0, 65.0, 202.0, 19.0, 68.0, 88.0, -1.0 };
	static double[] SAT1 = {40.0, 38.0, 35.0, 48.0, 194.0, 29.0, 31.0, 61.0, -1.0 };
	static double[] SAT2 = {32.0, 24.0, 29.0, 45.0, 232.0, 34.0, 31.0, 64.0, -1.0 };
	static double[] SAT3 = {11.0, 11.0, 21.0, 9.0, 21.0, 11.0, 12.0, 20.0, -1.0 };
	static double[] SAT4 = {12.0, 4.0, 11.0, 16.0, 108.0, 9.0, 11.0, 24.0, -1.0 };
	static double[] SAT5 = {14.0, 6.0, 9.0, 17.0, 117.0, 7.0, 15.0, 30.0, -1.0 };
	static double[] SAT6 = {20.0, 12.0, 28.0, 18.0, 110.0, 18.0, 19.0, 40.0, -1.0 };
	static double[] SAT7 = {15.0, 13.0, 43.0, 37.0, 113.0, 37.0, 21.0, 52.0, -1.0 };
	static double[] SAT8 = {14.0, 11.0, 7.0, 16.0, 114.0, 5.0, 16.0, 28.0, -1.0 };
	static double[] SAT9 = {17.0, 11.0, 11.0, 18.0, 106.0, 7.0, 13.0, 31.0, -1.0 };

	static double[] BinPacking0 = {0.03508571428571228, 0.03205947873799497, 0.007359777777777188, 0.01615080154781523, 0.0761606425702791, 0.016235268103923728, 0.036281773399012285, 0.07604198125836525, -1.0 };
	static double[] BinPacking1 = {0.03213484358144303, 0.03527296206617936, 0.006711001911001713, 0.011967538126361199, 0.07229495909210604, 0.01638750339581574, 0.03569416195856534, 0.08520838574423484, -1.0 };
	static double[] BinPacking2 = {0.025755741935484244, 0.026082290322580914, 0.023312774193548713, 0.02252603225806482, 0.029879322580644962, 0.02313745161290337, 0.026314774193548773, 0.03296529032258089, -1.0 };
	static double[] BinPacking3 = {0.028043081967213723, 0.027376852459016665, 0.02420196721311496, 0.023530262295081816, 0.05134174193548391, 0.023596426229508194, 0.028590721311475775, 0.05543274193548364, -1.0 };
	static double[] BinPacking4 = {0.01514577114427762, 0.014383582089552038, 0.004562463587182708, 0.0047538909696214215, 0.029646913580247958, 0.012857545605306497, 0.019300619578686673, 0.03605723657236659, -1.0 };
	static double[] BinPacking5 = {0.01576334563345627, 0.01163111111111026, 0.003197191243287878, 0.0031459727385378944, 0.03311404151404329, 0.015670848708486895, 0.017642476424763576, 0.03915377128953801, -1.0 };
	static double[] BinPacking6 = {0.17121571739130437, 0.17468305149051477, 0.011309494047618496, 0.026892796460177282, 0.18416823180593012, 0.09480794886363708, 0.17565084552845522, 0.19119587667560367, -1.0 };
	static double[] BinPacking7 = {0.15855611232876565, 0.15633559122084817, 0.02548132053175789, 0.054555811046510305, 0.17403583989145055, 0.11818678991596576, 0.162470174863386, 0.18351590296495734, -1.0 };
	static double[] BinPacking8 = {0.0749617300724863, 0.07574535989137787, 0.07337305253624815, 0.08769208988765931, 0.1242971365638974, 0.06202534121929382, 0.07663162895930031, 0.151842832394966, -1.0 };
	static double[] BinPacking9 = {0.02274456278028858, 0.024538577827564523, 0.01909303761932668, 0.02566519306101256, 0.04175743618202554, 0.01757702247191928, 0.02679099552574271, 0.04581057585826287, -1.0 };

	static double[] PersonnelScheduling0 = {3305.0, 3312.0, 3349.0, 3345.0, 3344.0, 5511.0, 3317.0, 3342.0, -1.0 };
	static double[] PersonnelScheduling1 = {2124.0, 2075.0, 2685.0, 1955.0, 2234.0, 9770.0, 2220.0, 2465.0, -1.0 };
	static double[] PersonnelScheduling2 = {360.0, 400.0, 1800.0, 305.0, 375.0, 520.0, 360.0, 290.0, -1.0 };
	static double[] PersonnelScheduling3 = {16.0, 16.0, 29.0, 23.0, 21.0, 54.0, 13.0, 18.0, -1.0 };
	static double[] PersonnelScheduling4 = {20.0, 20.0, 40.0, 19.0, 21.0, 59.0, 20.0, 18.0, -1.0 };
	static double[] PersonnelScheduling5 = {22.0, 18.0, 38.0, 22.0, 26.0, 64.0, 18.0, 26.0, -1.0 };
	static double[] PersonnelScheduling6 = {1201.0, 1103.0, 1116.0, 1201.0, 1126.0, 1472.0, 1123.0, 1351.0, -1.0 };
	static double[] PersonnelScheduling7 = {2271.0, 2286.0, 2357.0, 2265.0, 2308.0, 46455.0, 2205.0, 2831.0, -1.0 };
	static double[] PersonnelScheduling8 = {3138.0, 3219.0, 3356.0, 3164.0, 3258.0, 42015.0, 3246.0, 3819.0, -1.0 };
	static double[] PersonnelScheduling9 = {9482.0, 9717.0, 9797.0, 10055.0, 9679.0, 83542.0, 9839.0, 17276.0, -1.0};

	static double[] FlowShop0 = {6384.0, 6380.0, 6397.0, 6305.0, 6388.0, 6314.0, 6383.0, 6305.0, -1.0};
	static double[] FlowShop1 = {6322.0, 6327.0, 6395.0, 6276.0, 6335.0, 6242.0, 6323.0, 6290.0, -1.0};
	static double[] FlowShop2 = {6395.0, 6402.0, 6416.0, 6346.0, 6414.0, 6337.0, 6409.0, 6356.0, -1.0};
	static double[] FlowShop3 = {6387.0, 6388.0, 6390.0, 6353.0, 6393.0, 6332.0, 6376.0, 6335.0, -1.0};
	static double[] FlowShop4 = {6452.0, 6459.0, 6416.0, 6422.0, 6463.0, 6403.0, 6472.0, 6378.0, -1.0};
	static double[] FlowShop5 = {10537.0, 10541.0, 10537.0, 10497.0, 10544.0, 10497.0, 10540.0, 10562.0, -1.0};
	static double[] FlowShop6 = {10965.0, 10964.0, 10944.0, 10957.0, 10973.0, 10923.0, 10960.0, 10923.0, -1.0};
	static double[] FlowShop7 = {26462.0, 26463.0, 26464.0, 26363.0, 26452.0, 26402.0, 26424.0, 26342.0, -1.0};
	static double[] FlowShop8 = {26933.0, 26899.0, 26883.0, 26823.0, 26953.0, 26822.0, 26894.0, 26802.0, -1.0};
	static double[] FlowShop9 = {26751.0, 26745.0, 26791.0, 26637.0, 26771.0, 26678.0, 26780.0, 26696.0, -1.0};

	static double[][] SAT = {SAT0, SAT1, SAT2, SAT3, SAT4, SAT5, SAT6, SAT7, SAT8, SAT9};
	static double[][] BinPacking = {BinPacking0, BinPacking1, BinPacking2, BinPacking3, BinPacking4, BinPacking5, BinPacking6, BinPacking7, BinPacking8, BinPacking9};
	static double[][] PersonnelScheduling = {PersonnelScheduling0, PersonnelScheduling1, PersonnelScheduling2, PersonnelScheduling3, PersonnelScheduling4, PersonnelScheduling5, PersonnelScheduling6, PersonnelScheduling7, PersonnelScheduling8,PersonnelScheduling9};
	static double[][] FlowShop = {FlowShop0, FlowShop1, FlowShop2, FlowShop3, FlowShop4, FlowShop5, FlowShop6, FlowShop7, FlowShop8, FlowShop9};

	static double[][][] results = {SAT, BinPacking, PersonnelScheduling, FlowShop};

	public static class Score implements Comparable<Score> {
		int num;
		double score;
		public Score(int n, double s) {
			num = n;
			score = s;
		}
		public int compareTo(Score o) {
			Score obj = (Score)o;
			if (this.score < obj.score) {
				return -1;
			} else if (this.score == obj.score) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
