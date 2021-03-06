package com.yshstudio.originalproduct.tools;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class TextContent {

    public String content = "公司拥有强大的市场研究团队和专业电子商务运营队伍，目前在编员工数量超过500人，并且具备丰富的打造电子商务交易平台的实战经验。公司汇聚了大量B2C电子商务行业精英，已组成专业化的市场调研、分析、推广和客户服务团队，同时公司专注于技术实力的投资，以打造全国一流的电子商务交易平台另外，拥有一流的工作环境及完备的管理体系是公司始终坚持的团队原则。运用全新而独特的理念，致力为包括但不限于有意电子商务市场的厂家商户以及个人网民，提供专业优质的B2C交易服务。";
    public String qontect_li = "我们的理念：一切服从于市场，一切源于客户 我们的精神：创新，务实，合作，共赢";
    public String qontect_phone = "客服热线：400-12345678";
    public String qontect_emi = "邮箱: shequshangcheng@163.com";
    public String qontect_web = "公司网站：www.shequshangcheng.com";

    public List<ContentValues> setData() {
        List<ContentValues> as = new ArrayList<>();
        ContentValues one = new ContentValues();
        one.put("a", "广告合作");
        one.put("b", "2326895861@myee.net.cn");
        one.put("c", "");
        as.add(one);
        ContentValues one1 = new ContentValues();
        one1.put("a", "投资合作");
        one1.put("b", "3222808584@myee.net.cn");
        one1.put("c", "");
        as.add(one);
        ContentValues one2 = new ContentValues();
        one2.put("a", "加入我们");
        one2.put("b", "2326895861@myee.net.cn");
        one2.put("c", "欢迎有识之士");
        as.add(one2);
        ContentValues one3 = new ContentValues();
        one3.put("a", "战略合作");
        one3.put("b", "2326895861@myee.net.cn");
        one3.put("c", "运营、媒体、鉴定中心、机构等战略姓合作");
        as.add(one3);

        ContentValues one4 = new ContentValues();
        one4.put("a", "商务合作");
        one4.put("b", "2326895861@myee.net.cn");
        one4.put("c", "博客、自媒体、视频栏目等有关入驻、资源置换 内容输出等合作");
        as.add(one4);
        ContentValues one5 = new ContentValues();
        one5.put("a", "市场合作");
        one5.put("b", "2326895861@myee.net.cn");
        one5.put("c", "品牌合作、联合推广、渠道合作、活动推广、ap p换量合作");
        as.add(one5);
        return as;
    }

    /**
     * 知识产权
     */
    public String equities = "  承诺并保证，您在去卖艺网（www.myee.net.cn）及“去卖艺APP”移动客户端以各种形式发布的作品及信息，均符合《中华人民共和国著作权法》、《中华人民共和国商标法》、《中华人民共和国反不正当竞争法》、《中华人民共和国侵权责任法》、《中华人民共和国广告法》、《中华人民共和国计算机信息系统安全保护条例》、《计算机软件保护条例》及《信息网络传播权保护条例》等有关法律、法规、规章的规定。"
            + " 您承诺并保证，您在去卖艺网（www. myee.net.cn）及“去卖艺APP”移动客户端以各种形式发布的信息，均符合社会公序良俗。"
            + " 您承诺并保证，您在去卖艺网（www.myee.net.cn）及“去卖艺APP”移动客户端上传的作品、文字、图片、视频、音频、多媒体资料或其他内容均拥有相应合法的权利。这些文字、图片、视频、音频、多媒体资料或其他内容，不侵犯任何第三方的肖像权、名誉权、著作权、商标权等任何权利，不违反您与任何第三方所签订的任何法律文件的规定。"
            + "去卖艺公司有权对您上传的作品、文字、图片、视频、音频、多媒体资料或其他内容随时进行检查或编辑。如果发现您上传的内容不符合前述承诺，去卖艺公司有权删除您所上传的内容。对于您通过去卖艺网（www.myee.net.cn）及“去卖艺APP”移动客户端上传的内容，去卖艺公司不保证其合法性、正当性、准确性或完整性。您应自行承担因此产生的法律责任，如果因您违反上述承诺及保证导致去卖艺公司因此受到损失，您应赔偿由此造成的全部损失。"
            + "  您在点击确认本承诺之前，请仔细阅读本承诺，并确认您已完全理解本承诺。如您对本承诺有任何疑问，请立即停止通过去卖艺网（www.myee.net.cn）及“去卖艺APP”移动客户端上传的作品、文字、图片、视频、音频、多媒体资料或其他内容，并可向去卖艺网客服咨询。";
    /**
     * 申请卖主
     */
    public String shopEquities = "    买家在七天内点击退货，通过快递退货（需见物流单号）十天内，卖家未确认收货的，去卖艺网通知支付宝或者微信退款买家"
            + " 一、去卖艺网平台服务"
            + "1、通过去卖艺网及关联公司提供的去卖艺网平台服务和其它服务，会员可在去卖艺网平台上创建店铺、发布交易信息、查询商品和服务信息、达成交易意向并进行交易、对其他会员进行评价、参加去卖艺网组织的活动以及使用其它信息服务及技术服务，具体以所开通的平台提供的服务内容为准。"
            + "2、您在去卖艺网平台上交易过程中与其他会员发生交易纠纷时，一旦您或其它会员任一方或双方共同提交去卖艺网要求调处，则去卖艺网作为独立第三方，有权根据单方判断做出调处决定，您了解并同意接受去卖艺网的判断和调处决定。"
            + "3、您了解并同意，去卖艺网有权应政府部门（包括司法及行政部门）的要求，向其提供您向去卖艺网提供的用户信息和交易记录等必要信息。如您涉嫌侵犯他人知识产权等合法权益，则去卖艺网亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人提供您必要的身份信息。"
            + "4、您在使用去卖艺网平台服务过程中，所产生的应纳税赋，以及一切硬件、软件、服务及其它方面的费用，均由您独自承担"
            + " 二、去卖艺网平台服务使用规范"
            + "1、在去卖艺网平台上使用去卖艺网服务过程中，您承诺遵守以下约定："
            + "a)实施的所有行为均遵守国家法律、法规等规范性文件及去卖艺网平台各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不偷逃应缴税费，不违反本协议及相关规则。"
            + "b)在与其他会员交易过程中，遵守诚实信用原则，不采取不正当竞争行为，不扰乱网上交易的正常秩序，不从事与网上交易无关的行为。"
            + "c)不发布国家禁止销售的或限制销售的商品或服务信息（除非取得合法且足够的许可），不发布涉嫌侵犯他人知识产权或其它合法权益的商品或服务信息，不发布违背社会公共利益或公共道德或去卖艺网认为不适合在去卖艺网平台上销售的商品或服务信息，不发布其它涉嫌违法或违反本协议及各类规则的信息。"
            + "2、您了解并同意，去卖艺网有权应政府部门（包括司法及行政部门）的要求，向其提供您向去卖艺网提供的用户信息和交易记录等必要信息。如您涉嫌侵犯他人知识产权等合法权益，则去卖艺网亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人提供您必要的身份信息。"
            + "三、去卖艺网平台服务使用规范"
            + "1、在去卖艺网平台上使用去卖艺网服务过程中，您承诺遵守以下约定："
            + "a)您如果违反前述承诺，产生任何法律后果的，您应以自己的名义独立承担所有的法律责任，并确保去卖艺网免于因此产生任何损失或增加费用。"
            + "b）基于维护去卖艺网平台交易秩序及交易安全的需要，去卖艺网有权在发生恶意购买等扰乱市场正常交易秩序的情形下，执行关闭相应交易订单等操作。"
            + "c)经国家行政或司法机关的生效法律文书确认您存在违法或侵权行为，或者去卖艺网根据自身的判断，认为您的行为涉嫌违反法律法规的规定或涉嫌违反本协议和/或规则的条款的，则去卖艺网有权在去卖艺网平台上公示您的该等涉嫌违法或违约行为及去卖艺网已对您采取的措施。"
            + "d)对于您在去卖艺网平台上发布的涉嫌违法、涉嫌侵犯他人合法权利或违反本协议和/或规则的信息，去卖艺网有权不经通知您即予以删除，且按照规则的规定进行处罚。"
            + "e)对于您违反本协议项下承诺，或您在去卖艺网平台上实施的行为，包括您未在去卖艺网平台上实施但已经对去卖艺网平台及其用户产生影响的行为，去卖艺网有权单方认定您行为的性质及是否构成对本协议和/或规则的违反，并根据单方认定结果适用规则予以处理或终止向您提供服务，且无须征得您的同意或提前通知予您。您应自行保存与您行为有关的全部证据，并应对无法提供充要证据而承担的不利后果。"
            + "f)如您涉嫌违反有关法律或者本协议之规定，使去卖艺网遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿去卖艺网因此造成的损失和/或发生的费用，包括合理的律师费用。"
            + "您在点击确认本承诺之前，请仔细阅读本承诺，并确认您已完全理解本承诺。如您对本承诺有任何疑问，请立即停止通过去卖艺网（www.myee.net.cn）及“去卖艺APP”移动客户端上传的作品、文字、图片、视频、音频、多媒体资料或其他内容，并可向去卖艺网客服咨询。";

    public String deal = "注册协议\n" +
            "【审慎阅读】您在申请注册流程中点击同意前，应当认真阅读以下协议。请您务必审慎阅读、充分理解协议中相关条款内容，其中包括：\n" +
            "1、与您约定免除或限制责任的条款；\n" +
            "2、与您约定法律适用和管辖的条款；\n" +
            "3、其他以粗体下划线标识的重要条款。\n" +
            "如您对协议有任何疑问，可向平台客服咨询。\n" +
            "【特别提示】当您按照注册页面提示填写信息、阅读并同意协议且完成全部注册程序后，即表示您已充分阅读、理解并接受协议的全部内容。如您因平台服务与去卖艺发生争议的，适用《去卖艺平台服务协议》处理。如您在使用平台服务过程中与其他用户发生争议的，依您与其他用户达成的协议处理。\n" +
            "阅读协议的过程中，如果您不同意相关协议或其中任何条款约定，您应立即停止注册程序。\n" +
            "\u2028\u2028\n" +
            "去卖艺网平台服务协议\n" +
            "\n" +
            "\n" +
            "•提示条款\n" +
            "\n" +
            "欢迎您与去卖艺网平台经营者（详见定义条款）共同签署本《去卖艺网平台服务协议》（下称“本协议”）并使用去卖艺平台服务！\n" +
            "\n" +
            "本协议为《去卖艺网服务协议》修订版本，自本协议发布之日起，去卖艺网平台各处所称“去卖艺服务协议”均指本协议。\n" +
            "\n" +
            "各服务条款前所列索引关键词仅为帮助您理解该条款表达的主旨之用，不影响或限制本协议条款的含义或解释。为维护您自身权益，建议您仔细阅读各条款具体表述。\n" +
            "\n" +
            "【审慎阅读】您在申请注册流程中点击同意本协议之前，应当认真阅读本协议。请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体下划线标识，您应重点阅读。如您对协议有任何疑问，可向去卖艺平台客服咨询。\n" +
            "\n" +
            "【签约动作】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，即表示您已充分阅读、理解并接受本协议的全部内容，并与去卖艺达成一致，成为去卖艺网平台“用户”。阅读本协议的过程中，如果您不同意本协议或其中任何条款约定，您应立即停止注册程序。\n" +
            "\n" +
            "\n" +
            "一、 定义\n" +
            "\n" +
            "去卖艺网平台：指包括去卖艺网（域名为 myee.net.cn）、网站及移动客户端APP。\n" +
            "\n" +
            "去卖艺网：去卖艺网平台经营者的全称，深圳去卖艺网络科技有限公司。\n" +
            "\n" +
            "去卖艺网平台服务：去卖艺网基于互联网，以包含去卖艺网平台网站、移动客户端等在内的各种形态（包括未来技术发展出现的新的服务形态）向您提供的各项服务。\n" +
            "\n" +
            "去卖艺网平台规则：包括在所有去卖艺平台规则频道内已经发布及后续发布的全部规则、解读、公告等内容以及平台在帮派、论坛、帮助中心内发布的各类规则、实施细则、产品流程说明、公告等。\n" +
            "\n" +
            "去卖艺网规则：去卖艺网规则频道列明的各项规则，具体详见myee.neo.cn。\n" +
            "\n" +
            "\n" +
            "支付宝系统： 指提供支付宝服务的主体支付宝（中国）网络技术有限公司。\n" +
            "微信支付系统；指提供腾讯公司的支付产品—微信商户平台，分为PC端和手机移动端\n" +
            "同一用户：使用同一身份认证信息或经去卖艺平台排查认定多个去卖艺账户的实际控制人为同一人的，均视为同一用户。\n" +
            "\n" +
            "\n" +
            "二、 协议范围\n" +
            "\n" +
            "2.1 签约主体\n" +
            "\n" +
            "【平等主体】本协议由您与去卖艺网平台经营者共同缔结，本协议对您与去卖艺网平台经营者均具有合同效力。\n" +
            "\n" +
            "【主体信息】去卖艺网平台经营者是指经营去卖艺平台的各法律主体，您可随时查看去卖艺网平台各网站首页底部公示的证照信息以确定与您履约的去卖艺主体。本协议项下，去卖艺网平台经营者可能根据去卖艺平台的业务调整而发生变更，变更后的去卖艺网平台经营者与您共同履行本协议并向您提供服务，去卖艺网平台经营者的变更不会影响您本协议项下的权益。去卖艺网平台经营者还有可能因为提供新的去卖艺网平台服务而新增，如您使用新增的去卖艺网平台服务的，视为您同意新增的去卖艺网平台经营者与您共同履行本协议。发生争议时，您可根据您具体使用的服务及对您权益产生影响的具体行为对象确定与您履约的主体及争议相对方。\n" +
            "\n" +
            "2.2补充协议\n" +
            "\n" +
            "由于互联网高速发展，您与去卖艺网签署的本协议列明的条款并不能完整罗列并覆盖您与去卖艺所有权利与义务，现有的约定也不能保证完全符合未来发展的需求。因此，去卖艺网平台规则均为本协议的补充协议，与本协议不可分割且具有同等法律效力。如您使用去卖艺网平台服务，视为您同意上述补充协议。\n" +
            "\n" +
            "\n" +
            "三、 账户注册与使用\n" +
            "\n" +
            "3.1 用户资格\n" +
            "\n" +
            "您确认，在您开始注册程序使用去卖艺网平台服务前，您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担因此而导致的一切后果。\n" +
            "\n" +
            "3.2 账户说明\n" +
            "\n" +
            "【账户获得】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，您可获得去卖艺网平台账户并成为去卖艺平台用户。\n" +
            "\n" +
            "【账户使用】您有权使用您设置或确认的去卖艺会员名、邮箱、手机号码（以下简称“账户名称”）及您设置的密码（账户名称及密码合称“账户”）登录去卖艺平台。\n" +
            "【账户转让】由于用户账户关联用户信用信息，仅当有法律明文规定、司法裁定或经去卖艺同意，并符合去卖艺网平台规则规定的用户账户转让流程的情况下，您可进行账户的转让。您的账户一经转让，该账户项下权利义务一并转移。除此外，您的账户不得以任何方式转让，否则由此产生的一切责任均由您承担。\n" +
            "\n" +
            "【实名认证】作为去卖艺网平台经营者，法律并未赋予去卖艺强制要求所有用户进行支付宝实名认证的权力，但为使您更好地使用去卖艺平台的各项服务，去卖艺建议您按照支付宝公司、腾讯公司的支付产品—微信商户平台的要求及我国法律规定完成实名认证。\n" +
            "\n" +
            "【不活跃账户回收】如您的账户同时符合以下条件，则去卖艺网可回收您的账户，您的账户将不能再登录任一去卖艺平台，相应服务同时终止：\n" +
            "（一）未绑定通过实名认证的支付宝账户 微信支付；\n" +
            "（二）连续六个月未用于登录任一去卖艺网平台；\n" +
            "（三）不存在未到期的有效业务。\n" +
            "\n" +
            "3.3 注册信息管理\n" +
            "\n" +
            "3.3.1 真实合法\n" +
            "【信息真实】在使用去卖艺网平台服务时，您应当按去卖艺网平台页面的提示准确完整地提供您的信息（包括您的姓名及电子邮件地址、联系电话、联系地址等），以便去卖艺工作人员或其他用户与您联系。您了解并同意，您有义务保持您提供信息的真实性及有效性。\n" +
            "\n" +
            "【会员名的合法性】您设置的去卖艺网会员名不得违反国家法律法规及去卖艺网规则关于会员名的管理规定，否则去卖艺网可回收您的去卖艺会员名。去卖艺网会员名的回收不影响您以邮箱、手机号码登录去卖艺网平台并使用去卖艺网平台服务。\n" +
            "\n" +
            "3.3.2 更新维护\n" +
            "您应当及时更新您提供的信息，在法律有明确规定要求去卖艺网作为平台服务提供者必须对部分用户（如平台卖家等）的信息进行核实的情况下，去卖艺将依法不时地对您的信息进行检查核实，您应当配合提供最新、真实、完整的信息。\n" +
            "如去卖艺按您最后一次提供的信息与您联系未果、您未按去卖艺网 的要求及时提供信息、您提供的信息存在明显不实的，您将承担因此对您自身、他人及去卖艺造成的全部损失与不利后果。\n" +
            "\n" +
            "3.4 账户安全规范\n" +
            "\n" +
            "【账户安全保管义务】您的账户为您自行设置并由您保管，去卖艺网任何时候均不会主动要求您提供您的账户。因此，建议您务必保管好您的账户， 并确保您在每个上网时段结束时退出登录并以正确步骤离开去卖艺平台。\n" +
            "账户因您主动泄露或遭受他人攻击、诈骗等行为导致的损失及后果，均由您自行承担。\n" +
            "\n" +
            "【账户行为责任自负】除去卖艺网存在过错外，您应对您账户项下的所有行为结果（包括但不限于在线签署各类协议、发布信息、购买商品及服务及披露信息等）负责。\n" +
            "\n" +
            "【日常维护须知】如发现任何未经授权使用您账户登录去卖艺平台或其他可能导致您账户遭窃、遗失的情况，建议您立即通知去卖艺，并授权去卖艺将该信息同步给支付宝。您理解去卖艺网对您的任何请求采取行动均需要合理时间，除去卖艺网存在过错外，去卖艺网对在采取行动前已经产生的后果不承担任何责任。\n" +
            "\n" +
            "\n" +
            "四、 去卖艺网平台服务及规范\n" +
            "\n" +
            "【服务概况】您有权在去卖艺网平台上享受店铺管理、商品及/或服务的销售与推广、商品及/或服务的购买与评价、交易争议处理等服务。去卖艺网提供的服务内容众多，具体您可登录去卖艺网平台浏览。\n" +
            "\n" +
            "4.1 店铺管理\n" +
            "\n" +
            "【店铺创建】通过在去卖艺网创建店铺，您可发布一手原创类商品及一手内容/或服务信息并与其他用户达成交易。\n" +
            "基于去卖艺网管理需要，您理解并认可，同一用户在去卖艺网仅能开设一家店铺，去卖艺可关闭您在去卖艺网同时开设的其他店铺。\n" +
            "\n" +
            "【店铺转让】由于店铺转让实质为店铺经营者账户的转让，店铺转让的相关要求与限制请适用本协议3.2条账户转让条款。\n" +
            "\n" +
            "【店铺关停】您有权通过使用店铺打烊短暂关停您的店铺，您应当对您店铺关停前已达成的交易继续承担发货、退换货及质保维修、维权投诉处理等交易保障责任。\n" +
            "依据上述约定关停店铺均不会影响您已经累积的信用。\n" +
            "\n" +
            "4.2商品及/或服务的销售与推广\n" +
            "\n" +
            "【商品及/或服务信息发布】通过去卖艺网提供的服务，您有权通过文字、图片、视频、音频等形式在去卖艺网平台上发布商品及内容/或服务信息、招揽和参与社区内容互动并物色交易对象、达成交易。\n" +
            "\n" +
            "【禁止销售范围】您应当确保您对您在去卖艺网平台上发布的商品及/或服务享有相应的权利，您不得在去卖艺平台上销售以下商品及/或提供以下服务：\n" +
            "（一）国家禁止或限制的；\n" +
            "（二）侵犯他人知识产权或其它合法权益的；\n" +
            "（三）去卖艺网平台规则或各平台与您单独签署的协议中已明确说明不适合在去卖艺网平台上发布粗俗不雅内容和视频及销售/或提供的。\n" +
            "\n" +
            "【交易秩序保障】您应当遵守诚实信用原则，确保您所发布的商品及内容/或服务信息真实、与您实际所销售的商品及内容/或提供的服务相符，并在交易过程中切实履行您的交易承诺。\n" +
            "您应当维护去卖艺网原创品平台市场良性竞争秩序，不得贬低、诋毁竞争对手，不得干扰去卖艺网平台上进行的任何交易、活动，不得以任何不正当方式提升或试图提升自身的信用度，不得以任何方式干扰或试图干扰去卖艺网平台的正常运作。\n" +
            "\n" +
            "【促销及推广】您有权自行决定商品及内容/或服务的促销及推广方式，去卖艺网亦为您提供了形式丰富的促销推广工具。您的促销推广行为应当符合国家相关法律法规及淘宝平台的要求。\n" +
            "\n" +
            "【依法纳税】依法纳税是每一个公民、企业应尽的义务，您应对销售额/营业额超过法定免征额部分及时、足额地向税务主管机关申报纳税。\n" +
            "\n" +
            "4.3商品及内容/或服务的购买与评价\n" +
            "\n" +
            "【商品及内容/或服务的购买】当您在去卖艺网平台发布内容及购买商品/或服务时，请您务必仔细确认所购商品的品名、价格、数量、型号、规格、尺寸或服务的时间、内容、限制性要求等重要事项，并在下单时核实您的联系地址、电话、收货人等信息。如您填写的收货人非您本人，则该收货人的行为和意思表示产生的法律后果均由您承担。\n" +
            "您的购买行为应当基于真实的消费需求，不得存在对商品及/或服务实施恶意购买、恶意维权等扰乱去卖艺网平台正常交易秩序的行为。基于维护去卖艺网平台交易秩序及交易安全的需要，去卖艺网发现上述情形时可主动执行关闭相关交易订单等操作。\n" +
            "\n" +
            "【一口价与议价】去卖艺网平台存在“一口价”或有“议价”两种出价形式。在议价形式下，您理解去卖艺网平台并非《中华人民共和国拍卖法》规定的“拍卖人”， 去卖艺网平台仅为用户以竞价形式购买商品及/或服务的在线交易场所。\n" +
            "\n" +
            "【评价】您有权在去卖艺网平台提供的评价系统中对与您达成交易的其他用户商品及/或服务进行评价。您应当理解，您在去卖艺网平台的评价信息是公开的，如您不愿意在评价信息中向公众披露您的身份信息，您有权选择通过匿名形式发表评价内容。\n" +
            "您的所有评价行为应遵守去卖艺网平台规则的相关规定，评论、评价内容应当客观真实，不应包含任何污言秽语、色情低俗、广告信息及法律法规与本协议列明的其他禁止性信息；您不应以不正当方式帮助他人提升信用或利用评价权利对其他用户实施威胁、敲诈勒索。去卖艺网可按照平台规则的相关规定对您实施上述行为所产生的评价信息进行删除或屏蔽。\n" +
            "\n" +
            "4.4交易争议处理\n" +
            "\n" +
            "【交易争议处理途径】您在去卖艺网平台交易过程中与其他用户发生争议的，您或其他用户中任何一方均有权选择以下途径解决：\n" +
            "（一）与争议相对方自主协商；\n" +
            "（二）使用去卖艺网平台提供的争议调处服务；\n" +
            "（三）请求消费者协会或者其他依法成立的调解组织调解；\n" +
            "（四）向有关行政部门投诉；\n" +
            "（五）根据与争议相对方达成的仲裁协议（如有）提请仲裁机构仲裁；\n" +
            "（六）向人民法院提起诉讼。\n" +
            "\n" +
            "【平台后台服务】如您选择使用去卖艺网平台的争议调处服务，则表示您认可去卖艺网平台的客服或大众评审员（“调处方”）作为独立的第三方根据其所了解到的争议事实并依据去卖艺平台规则所作出的调处决定（包括调整相关订单的交易状态、判定将争议款项的全部或部分支付给交易一方或双方等）。在去卖艺网平台调处决定作出前，您可选择其他途径解决争议以中止去卖艺平台的争议调处服务。\n" +
            "如您对调处决定不满意，您仍有权采取其他争议处理途径解决争议，但通过其他争议处理途径未取得终局决定前，您仍应先履行调处决定。\n" +
            "\n" +
            "4.5费用\n" +
            "\n" +
            "去卖艺网为去卖艺网平台向您提供的服务付出了大量的成本，除去卖艺网平台明示的收费业务外，去卖艺向您提供的服务目前是免费的。如未来去卖艺向您收取合理费用，去卖艺会采取合理途径并以足够合理的期限提前通过法定程序并以本协议第八条约定的方式通知您，确保您有充分选择的权利。\n" +
            "\n" +
            "4.6责任限制\n" +
            "\n" +
            "【不可抗力】去卖艺网负责\"按现状\"和\"可得到\"的状态向您提供去卖艺网平台服务。去卖艺网依法律规定承担基础保障义务，但无法对由于信息网络设备维护、连接故障，电脑、通讯或其他系统的故障，电力故障，罢工，暴乱，火灾，洪水，风暴，爆炸，战争，政府行为，司法行政机关的命令或因第三方原因而给您造成的损害结果承担责任。\n" +
            "\n" +
            "【海量信息】去卖艺网仅向您提供去卖艺平台服务，您了解去卖艺网平台上的信息系用户自行发布，且可能存在风险和瑕疵。鉴于去卖艺网平台具备存在海量信息及信息网络环境下信息与实物相分离的特点，去卖艺网无法逐一审查商品及/或服务的信息，无法逐一审查交易所涉及的商品及/或服务的质量、安全以及合法性、真实性、准确性，对此您应谨慎判断。\n" +
            "\n" +
            "【调处决定】您理解并同意，在争议调处服务中，去卖艺网平台的客服、大众评审员并非专业人士，仅能以普通人的认知对用户提交的凭证进行判断。因此，除存在故意外，调处方对争议调处决定免责。\n" +
            "\n" +
            "\n" +
            "五、 用户信息的保护及授权\n" +
            "\n" +
            "5.1个人信息的保护\n" +
            "\n" +
            "去卖艺网非常重视用户个人信息（即能够独立或与其他信息结合后识别用户身份的信息）的保护，在您使用去卖艺网提供的服务时，您同意去卖艺网按照在去卖艺网平台上公布的隐私权政策收集、存储、使用、披露和保护您的个人信息。去卖艺网希望通过隐私权政策向您清楚地介绍去卖艺网对您个人信息的处理方式，因此去卖艺网建议您完整地阅读隐私权政策（点击myee.net.cn或点击去卖艺平台首页底部链接），以帮助您更好地保护您的隐私权。\n" +
            "\n" +
            "5.2非个人信息的保证与授权\n" +
            "\n" +
            "【信息的发布】您声明并保证，您对您所发布的信息拥有相应、合法的权利。否则，去卖艺网可对您发布的信息依法或依本协议进行删除或屏蔽。\n" +
            "\n" +
            "【禁止性信息】您应当确保您所发布的信息不包含以下内容：\n" +
            "（一）违反国家法律法规禁止性规定的；\n" +
            "（二）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；\n" +
            "（三）欺诈、虚假、不准确或存在误导性的；\n" +
            "（四）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；\n" +
            "（五）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；\n" +
            "（六）存在可能破坏、篡改、删除、影响去卖艺平台任何系统正常运行或未经授权秘密获取去卖艺平台及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；\n" +
            "（七）其他违背社会公共利益或公共道德或依据相关去卖艺平台协议、规则的规定不适合在去卖艺网平台上发布的。\n" +
            "\n" +
            "【授权使用】对于您提供及发布除个人信息外的文字、图片、视频、音频等非个人信息，在版权保护期内您免费授予去卖艺网及其关联公司、微信支付 支付宝公司获得全球排他的许可使用权利及再授权给其他第三方使用的权利。您同意去卖艺网及其关联公司、支付宝公司存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。\n" +
            "为方便您使用去卖艺网平台、支付宝等其他相关服务，您授权去卖艺网将您在账户注册和使用去卖艺平网台服务过程中提供、形成的信息传递给去卖艺平台、支付宝等其他相关服务提供者，或从去卖艺网平台、支付宝等其他相关服务提供者获取您在注册、使用相关服务期间提供、形成的信息。\n" +
            "\n" +
            "\n" +
            "六、 用户的违约及处理\n" +
            "\n" +
            "6.1 违约认定\n" +
            "\n" +
            "发生如下情形之一的，视为您违约：\n" +
            "（一）使用去卖艺网平台服务时违反有关法律法规规定的；\n" +
            "（二）违反本协议或本协议补充协议（即本协议第2.2条）约定的。\n" +
            "为适应电子商务发展和满足海量用户对高效优质服务的需求，您理解并同意，去卖艺可在去卖艺网平台规则中约定违约认定的程序和标准。如：去卖艺网可依据您的用户数据与海量用户数据的关系来认定您是否构成违约；您有义务对您的数据异常现象进行充分举证和合理解释，否则将被认定为违约。\n" +
            "\n" +
            "6.2 违约处理措施\n" +
            "\n" +
            "【信息处理】您在去卖艺网平台上发布的信息构成违约的，去卖艺网可根据相应规则立即对相应信息进行删除、屏蔽处理或对您的商品进行下架、监管。\n" +
            "\n" +
            "【行为限制】您在去卖艺网平台上实施的行为，或虽未在去卖艺网平台上实施但对去卖艺网平台及其用户产生影响的行为构成违约的，去卖艺可依据相应规则对您执行账户扣分、限制参加营销活动、中止向您提供部分或全部服务、划扣违约金等处理措施。如您的行为构成根本违约的，去卖艺网可查封您的账户，终止向您提供服务。\n" +
            "\n" +
            "【支付宝账户/微信支付系统处理】当您违约的同时存在欺诈、售假、盗用他人账户等特定情形或您存在危及他人交易安全或账户安全风险时，去卖艺网会依照您行为的风险程度指示支付宝公司对您的支付宝账户采取取消收款、资金止付等强制措施。\n" +
            "\n" +
            "【处理结果公示】去卖艺网可将对您上述违约行为处理措施信息以及其他经国家行政或司法机关生效法律文书确认的违法信息在去卖艺网平台上予以公示。\n" +
            "\n" +
            "6.3赔偿责任\n" +
            "\n" +
            "如您的行为使去卖艺网及/或其关联公司、支付宝公司 微信支付公司 遭受损失（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），您应赔偿去卖艺网及/或其关联公司、支付宝公司的上述全部损失。\n" +
            "如您的行为使去卖艺网及/或其关联公司、支付宝公司遭受第三人主张权利，去卖艺网及/或其关联公司、支付宝公司/微信支付系统可在对第三人承担金钱给付等义务后就全部损失向您追偿。\n" +
            "如因您的行为使得第三人遭受损失或您怠于履行调处决定、去卖艺网及/或其关联公司出于社会公共利益保护或消费者权益保护目的，可指示支付宝公司/微信支付系统自您的支付宝账户中划扣相应款项进行支付。如您的支付余额或保证金不足以支付相应款项的，您同意委托去卖艺网使用自有资金代您支付上述款项，您应当返还该部分费用并赔偿因此造成去卖艺网的全部损失。\n" +
            "您同意去卖艺网指示支付宝公司/微信支付系统自您的支付宝账户中划扣相应款项支付上述赔偿款项。如您支付宝账户中的款项不足以支付上述赔偿款项的，去卖艺及/或关联公司可直接抵减您在去卖艺及/或其关联公司其它协议项下的权益，并可继续追偿。\n" +
            "\n" +
            "6.4特别约定\n" +
            "\n" +
            "【商业贿赂】如您向去卖艺网及/或其关联公司的雇员或顾问等提供实物、现金、现金等价物、劳务、旅游等价值明显超出正常商务洽谈范畴的利益，则可视为您存在商业贿赂行为。发生上述情形的，去卖艺可立即终止与您的所有合作并向您收取违约金及/或赔偿金，该等金额以去卖艺网因您的贿赂行为而遭受的经济损失和商誉损失作为计算依据。\n" +
            "\n" +
            "【关联处理】如您因严重违约导致去卖艺网终止本协议时，出于维护平台秩序及保护消费者权益的目的，去卖艺网及/或其关联公司可对与您在其他协议项下的合作采取中止甚或终止协议的措施，并以本协议第八条约定的方式通知您。\n" +
            "如去卖艺网与您签署的其他协议及去卖艺网及/或其关联公司、支付宝公司/微信支付系统与您签署的协议中明确约定了对您在本协议项下合作进行关联处理的情形，则去卖艺出于维护平台秩序及保护消费者权益的目的，可在收到指令时中止甚至终止协议，并以本协议第八条约定的方式通知您。\n" +
            "\n" +
            "\n" +
            "七、 协议的变更\n" +
            "\n" +
            "去卖艺网可根据国家法律法规变化及维护交易秩序、保护消费者权益需要，不时修改本协议、补充协议，变更后的协议、补充协议（下称“变更事项”）将通过法定程序并以本协议第八条约定的方式通知您。\n" +
            "如您不同意变更事项，您有权于变更事项确定的生效日前联系去卖艺反馈意见。如反馈意见得以采纳，去卖艺将酌情调整变更事项。\n" +
            "如您对已生效的变更事项仍不同意的，您应当于变更事项确定的生效之日起停止使用去卖艺平台服务，变更事项对您不产生效力；如您在变更事项生效后仍继续使用去卖艺平台服务，则视为您同意已生效的变更事项。\n" +
            "\n" +
            "\n" +
            "八、 通知\n" +
            "\n" +
            "您同意去卖艺网以以下合理的方式向您送达各类通知：\n" +
            "（一）公示的文案；\n" +
            "（二）站内信、弹出消息、客户端推送的消息；\n" +
            "（三）根据您预留于去卖艺网平台的联系方式发出的电子邮件、短信、函件等。\n" +
            "\n" +
            "\n" +
            "九、 协议的终止\n" +
            "\n" +
            "9.1 终止的情形\n" +
            "\n" +
            "【用户发起的终止】您有权通过以下任一方式终止本协议：\n" +
            "（一）在满足去卖艺网公示的账户注销条件时您通过网站自动服务注销您的账户的；\n" +
            "（二）变更事项生效前您停止使用并明示不愿接受变更事项的；\n" +
            "（三）您明示不愿继续使用去卖艺网平台服务，且符合去卖艺网终止条件的。\n" +
            "\n" +
            "【去卖艺网发起的终止】出现以下情况时，去卖艺网可以本协议第八条的所列的方式通知您终止本协议：\n" +
            "（一）您违反本协议约定，去卖艺依据违约条款终止本协议的；\n" +
            "（二）您盗用他人账户、发布违禁信息、骗取他人财物、售假、扰乱市场秩序、采取不正当手段谋利等行为，淘宝依据淘宝平台规则对您的账户予以查封的；\n" +
            "（三）除上述情形外，因您多次违反去卖艺网平台规则相关规定且情节严重，去卖艺网依据去卖艺平台规则对您的账户予以查封的；\n" +
            "（四）您的账户被去卖艺网依据本协议回收的；\n" +
            "（五）您在支付宝/微信支付系统或去卖艺网平台有欺诈、发布或销售假冒伪劣/侵权商品、侵犯他人合法权益或其他严重违法违约行为的；\n" +
            "（六）其它应当终止服务的情况。\n" +
            "\n" +
            "9.2 协议终止后的处理\n" +
            "\n" +
            "【用户信息披露】本协议终止后，除法律有明确规定外，去卖艺网无义务向您或您指定的第三方披露您账户中的任何信息。\n" +
            "\n" +
            "【去卖艺权利】本协议终止后，去卖艺网仍享有下列权利：\n" +
            "（一）继续保存您留存于去卖艺网平台的本协议第五条所列的各类信息；\n" +
            "（二）对于您过往的违约行为，去卖艺网仍可依据本协议向您追究违约责任。\n" +
            "\n" +
            "【交易处理】本协议终止后，对于您在本协议存续期间产生的交易订单，去卖艺网可通知交易相对方并根据交易相对方的意愿决定是否关闭该等交易订单；如交易相对方要求继续履行的，则您应当就该等交易订单继续履行本协议及交易订单的约定，并承担因此产生的任何损失或增加的任何费用。\n" +
            "\n" +
            "\n" +
            "十、 法律适用、管辖与其他\n" +
            "\n" +
            "【法律适用】本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国大陆地区法律；如法律无相关规定的，参照商业惯例及/或行业惯例。\n" +
            "\n" +
            "【管辖】您因使用去卖艺网平台服务所产生及与去卖艺网平台服务有关的争议，由去卖艺网 与您协商解决。协商不成时，任何一方均可向被告所在地人民法院提起诉讼。\n" +
            "\n" +
            "【可分性】本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。";


}
