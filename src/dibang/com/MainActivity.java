package dibang.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private ListView listviews;

    private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Bitmap mIcon1;
        private Bitmap mIcon2;
        private Bitmap mFlag;

        public EfficientAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            // Icons bound to the rows.
            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_1);
            mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
            mFlag = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_list_item_arrow);
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return DATA.length;
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.flag = (ImageView) convertView.findViewById(R.id.flag);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            holder.text.setText(DATA[position]);
            holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
            holder.flag.setImageBitmap(mFlag);

            return convertView;
        }

        static class ViewHolder {
            TextView text;
            ImageView icon;
            ImageView flag;
        }
    }


    private static final String[] DATA = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
            "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh",
            "Anthoriro", "Appenzell", "Aragon", "Ardi Gasna", "Ardrahan",
            "Armenian String", "Aromes au Gene de Marc", "Asadero", "Asiago",
            "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss", "Babybel",
            "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal",
            "Banon", "Barry's Bay Cheddar", "Basing", "Basket Cheese",
            "Bath Cheese", "Bavarian Bergkase", "Baylough", "Beaufort",
            "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir",
            "Bierkase", "Bishop Kennedy", "Blarney", "Bleu d'Auvergne",
            "Bleu de Gex", "Bleu de Laqueuille", "Bleu de Septmoncel",
            "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
            "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini",
            "Bocconcini (Australian)", "Boeren Leidenkaas", "Bonchester",
            "Bosworth", "Bougon", "Boule Du Roves", "Boulette d'Avesnes",
            "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",
            "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois",
            "Brebis du Puyfaucon", "Bresse Bleu", "Brick", "Brie",
            "Brie de Meaux", "Brie de Melun", "Brillat-Savarin", "Brin",
            "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",
            "Briquette de Brebis", "Briquette du Forez", "Broccio",
            "Broccio Demi-Affine", "Brousse du Rove", "Bruder Basil",
            "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
            "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase",
            "Button (Innes)", "Buxton Blue", "Cabecou", "Caboc", "Cabrales",
            "Cachaille", "Caciocavallo", "Caciotta", "Caerphilly",
            "Cairnsmore", "Calenzana", "Cambazola", "Camembert de Normandie",
            "Canadian Cheddar", "Canestrato", "Cantal", "Caprice des Dieux",
            "Capricorn Goat", "Capriole Banon", "Carre de l'Est",
            "Casciotta di Urbino", "Cashel Blue", "Castellano", "Castelleno",
            "Castelmagno", "Castelo Branco", "Castigliano", "Cathelain",
            "Celtic Promise", "Cendre d'Olivet", "Cerney", "Chabichou",
            "Chabichou du Poitou", "Chabis de Gatine", "Chaource", "Charolais",
            "Chaumes", "Cheddar", "Cheddar Clothbound", "Cheshire", "Chevres",
            "Chevrotin des Aravis", "Chontaleno", "Civray",
            "Coeur de Camembert au Calvados", "Coeur de Chevre", "Colby",
            "Cold Pack", "Comte", "Coolea", "Cooleney", "Coquetdale",
            "Corleggy", "Cornish Pepper", "Cotherstone", "Cotija",
            "Cottage Cheese", "Cottage Cheese (Australian)", "Cougar Gold",
            "Coulommiers", "Coverdale", "Crayeux de Roncq", "Cream Cheese",
            "Cream Havarti", "Crema Agria", "Crema Mexicana", "Creme Fraiche",
            "Crescenza", "Croghan", "Crottin de Chavignol",
            "Crottin du Chavignol", "Crowdie", "Crowley", "Cuajada", "Curd",
            "Cure Nantais", "Curworthy", "Cwmtawe Pecorino",
            "Cypress Grove Chevre", "Danablu (Danish Blue)", "Danbo",
            "Danish Fontina", "Daralagjazsky", "Dauphin", "Delice des Fiouves",
            "Denhany Dorset Drum", "Derby", "Dessertnyj Belyj", "Devon Blue",
            "Devon Garland", "Dolcelatte", "Doolin", "Doppelrhamstufel",
            "Dorset Blue Vinney", "Double Gloucester", "Double Worcester",
            "Dreux a la Feuille", "Dry Jack", "Duddleswell", "Dunbarra",
            "Dunlop", "Dunsyre Blue", "Duroblando", "Durrus",
            "Dutch Mimolette (Commissiekaas)", "Edam", "Edelpilz",
            "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne",
            "Esbareich", "Esrom", "Etorki", "Evansdale Farmhouse Brie",
            "Evora De L'Alentejo", "Exmoor Blue", "Explorateur", "Feta",
            "Feta (Australian)", "Figue", "Filetta", "Fin-de-Siecle",
            "Finlandia Swiss", "Finn", "Fiore Sardo", "Fleur du Maquis",
            "Flor de Guia", "Flower Marie", "Folded",
            "Folded cheese with mint", "Fondant de Brebis", "Fontainebleau",
            "Fontal", "Fontina Val d'Aosta", "Formaggio di capra", "Fougerus",
            "Four Herb Gouda", "Fourme d' Ambert", "Fourme de Haute Loire",
            "Fourme de Montbrison", "Fresh Jack", "Fresh Mozzarella",
            "Fresh Ricotta", "Fresh Truffles", "Fribourgeois", "Friesekaas",
            "Friesian", "Friesla", "Frinault", "Fromage a Raclette",
            "Fromage Corse", "Fromage de Montagne de Savoie", "Fromage Frais",
            "Fruit Cream Cheese", "Frying Cheese", "Fynbo", "Gabriel",
            "Galette du Paludier", "Galette Lyonnaise",
            "Galloway Goat's Milk Gems", "Gammelost", "Gaperon a l'Ail",
            "Garrotxa", "Gastanberra", "Geitost", "Gippsland Blue", "Gjetost",
            "Gloucester", "Golden Cross", "Gorgonzola", "Gornyaltajski",
            "Gospel Green", "Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",
            "Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",
            "Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh",
            "Greve", "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny",
            "Halloumi", "Halloumy (Australian)", "Haloumi-Style Cheese",
            "Harbourne Blue", "Havarti", "Heidi Gruyere", "Hereford Hop",
            "Herrgardsost", "Herriot Farmhouse", "Herve", "Hipi Iti",
            "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
            "Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu",
            "Isle of Mull", "Jarlsberg", "Jermi Tortes", "Jibneh Arabieh",
            "Jindi Brie", "Jubilee Blue", "Juustoleipa", "Kadchgall", "Kaseri",
            "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
            "Kikorangi", "King Island Cape Wickham Brie", "King River Gold",
            "Klosterkaese", "Knockalara", "Kugelkase", "L'Aveyronnais",
            "L'Ecir de l'Aubrac", "La Taupiniere", "La Vache Qui Rit",
            "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",
            "Langres", "Lappi", "Laruns", "Lavistown", "Le Brin",
            "Le Fium Orbo", "Le Lacandou", "Le Roule", "Leafield", "Lebbene",
            "Leerdammer", "Leicester", "Leyden", "Limburger",
            "Lincolnshire Poacher", "Lingot Saint Bousquet d'Orb", "Liptauer",
            "Little Rydings", "Livarot", "Llanboidy", "Llanglofan Farmhouse",
            "Loch Arthur Farmhouse", "Loddiswell Avondale", "Longhorn",
            "Lou Palou", "Lou Pevre", "Lyonnais", "Maasdam", "Macconais",
            "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",
            "Manouri", "Manur", "Marble Cheddar", "Marbled Cheeses",
            "Maredsous", "Margotin", "Maribo", "Maroilles", "Mascares",
            "Mascarpone", "Mascarpone (Australian)", "Mascarpone Torta",
            "Matocq", "Maytag Blue", "Meira", "Menallack Farmhouse",
            "Menonita", "Meredith Blue", "Mesost", "Metton (Cancoillotte)",
            "Meyer Vintage Gouda", "Mihalic Peynir", "Milleens", "Mimolette",
            "Mine-Gabhar", "Mini Baby Bells", "Mixte", "Molbo",
            "Monastery Cheeses", "Mondseer", "Mont D'or Lyonnais", "Montasio",
            "Monterey Jack", "Monterey Jack Dry", "Morbier",
            "Morbier Cru de Montagne", "Mothais a la Feuille", "Mozzarella",
            "Mozzarella (Australian)", "Mozzarella di Bufala",
            "Mozzarella Fresh, in water", "Mozzarella Rolls", "Munster",
            "Murol", "Mycella", "Myzithra", "Naboulsi", "Nantais",
            "Neufchatel", "Neufchatel (Australian)", "Niolo", "Nokkelost",
            "Northumberland", "Oaxaca", "Olde York", "Olivet au Foin",
            "Olivet Bleu", "Olivet Cendre", "Orkney Extra Mature Cheddar",
            "Orla", "Oschtjepka", "Ossau Fermier", "Ossau-Iraty", "Oszczypek",
            "Oxford Blue", "P'tit Berrichon", "Palet de Babligny", "Paneer",
            "Panela", "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)",
            "Parmigiano Reggiano", "Pas de l'Escalette", "Passendale",
            "Pasteurized Processed", "Pate de Fromage", "Patefine Fort",
            "Pave d'Affinois", "Pave d'Auge", "Pave de Chirac",
            "Pave du Berry", "Pecorino", "Pecorino in Walnut Leaves",
            "Pecorino Romano", "Peekskill Pyramid", "Pelardon des Cevennes",
            "Pelardon des Corbieres", "Penamellera", "Penbryn", "Pencarreg",
            "Perail de Brebis", "Petit Morin", "Petit Pardou", "Petit-Suisse",
            "Picodon de Chevre", "Picos de Europa", "Piora",
            "Pithtviers au Foin", "Plateau de Herve", "Plymouth Cheese",
            "Podhalanski", "Poivre d'Ane", "Polkolbin", "Pont l'Eveque",
            "Port Nicholson", "Port-Salut", "Postel", "Pouligny-Saint-Pierre",
            "Pourly", "Prastost", "Pressato", "Prince-Jean",
            "Processed Cheddar", "Provolone", "Provolone (Australian)",
            "Pyengana Cheddar", "Pyramide", "Quark", "Quark (Australian)",
            "Quartirolo Lombardo", "Quatre-Vents", "Quercy Petit",
            "Queso Blanco", "Queso Blanco con Frutas --Pina y Mango",
            "Queso de Murcia", "Queso del Montsec", "Queso del Tietar",
            "Queso Fresco", "Queso Fresco (Adobera)", "Queso Iberico",
            "Queso Jalapeno", "Queso Majorero", "Queso Media Luna",
            "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette",
            "Ragusano", "Raschera", "Reblochon", "Red Leicester",
            "Regal de la Dombes", "Reggianito", "Remedou", "Requeson",
            "Richelieu", "Ricotta", "Ricotta (Australian)", "Ricotta Salata",
            "Ridder", "Rigotte", "Rocamadour", "Rollot", "Romano",
            "Romans Part Dieu", "Roncal", "Roquefort", "Roule",
            "Rouleau De Beaulieu", "Royalp Tilsit", "Rubens", "Rustinu",
            "Saaland Pfarr", "Saanenkaese", "Saga", "Sage Derby",
            "Sainte Maure", "Saint-Marcellin", "Saint-Nectaire",
            "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
            "Sap Sago", "Sardo", "Sardo Egyptian", "Sbrinz", "Scamorza",
            "Schabzieger", "Schloss", "Selles sur Cher", "Selva", "Serat",
            "Seriously Strong Cheddar", "Serra da Estrela", "Sharpam",
            "Shelburne Cheddar", "Shropshire Blue", "Siraz", "Sirene",
            "Smoked Gouda", "Somerset Brie", "Sonoma Jack",
            "Sottocenare al Tartufo", "Soumaintrain", "Sourire Lozerien",
            "Spenwood", "Sraffordshire Organic", "St. Agur Blue Cheese",
            "Stilton", "Stinking Bishop", "String", "Sussex Slipcote",
            "Sveciaost", "Swaledale", "Sweet Style Swiss", "Swiss",
            "Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",
            "Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea",
            "Testouri", "Tete de Moine", "Tetilla", "Texas Goat Cheese",
            "Tibet", "Tillamook Cheddar", "Tilsit", "Timboon Brie", "Toma",
            "Tomme Brulee", "Tomme d'Abondance", "Tomme de Chevre",
            "Tomme de Romans", "Tomme de Savoie", "Tomme des Chouans",
            "Tommes", "Torta del Casar", "Toscanello", "Touree de L'Aubier",
            "Tourmalet", "Trappe (Veritable)", "Trois Cornes De Vendee",
            "Tronchon", "Trou du Cru", "Truffe", "Tupi", "Turunmaa",
            "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",
            "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco",
            "Vendomois", "Vieux Corse", "Vignotte", "Vulscombe",
            "Waimata Farmhouse Blue", "Washed Rind Cheese (Australian)",
            "Waterloo", "Weichkaese", "Wellington", "Wensleydale",
            "White Stilton", "Whitestone Farmhouse", "Wigmore",
            "Woodside Cabecou", "Xanadu", "Xynotyro", "Yarg Cornish",
            "Yarra Valley Pyramid", "Yorkshire Blue", "Zamorano",
            "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		listviews = (ListView) findViewById(R.id.listviews);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_view, new String[] { "img", "username" },
				new int[] { R.id.img, R.id.username });

		listviews.setAdapter(new EfficientAdapter(this));
		
		listviews.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				if(arg2 == 6) 
				{
					Intent intent = new Intent(MainActivity.this,ContactOur.class);
					MainActivity.this.startActivity(intent);
				}
			}
		});
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("img", R.drawable.icon);
		map.put("username", "网站设计");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "三维动画");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "效 果 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "戶 型 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "电 子 杂 知");
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "合 作 伙 伴");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "联 系 我 们");
		list.add(map);
		
		return list;
	}
}