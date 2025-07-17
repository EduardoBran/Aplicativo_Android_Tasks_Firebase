package com.luizeduardobrandao.tasksfirebasehilt.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(
    fragmentActivity: FragmentActivity          // Activity que hospeda o ViewPager2
) : FragmentStateAdapter(fragmentActivity) {

    // Lista que armazena cada Fragment que será exibido em uma aba
    private val fragmentList: MutableList<Fragment> = ArrayList()
    // Lista que armazena o recurso de string (ID) usado como título da aba
    private val titleList: MutableList<Int> = ArrayList()


    // - Retorna o título (ID de string) da aba na posição informada.
    // - Será usado pelo TabLayoutMediator para definir o texto da aba.
    fun getTitle(position: Int): Int {
        return titleList[position]
    }

    // - Adiciona um par (fragment + título) ao adapter.
    // - Deve ser chamado antes de setar o adapter no ViewPager2.
    // * @param fragment  o Fragment que exibirá o conteúdo da aba
    // * @param title     o ID de string (R.string.xxx) que será exibido na aba
    fun addFragment(fragment: Fragment, title: Int) {
        fragmentList.add(fragment)
        titleList.add(title)
    }


    // - Retorna o total de páginas (abas) gerenciadas por este adapter.
    override fun getItemCount(): Int {
        return fragmentList.size
    }


    // - Instancia e retorna o Fragment associado à posição.
    // - O ViewPager2 chama esse mét0do conforme o usuário desliza entre as abas.
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}